<template>
    <div id="main">
        <el-page-header @back="$router.back()"
                style="padding: 1em 1em 0.5em 1em;">
            <template #content>
                <span class="text-large font-600 mr-3">{{ article.title }}</span>
            </template>
            <template #extra>
                <div class="flex items-center" v-show="updateVisible">
                    <el-button type="primary" 
                            class="ml-2" 
                            :icon="Edit"
                            @click="$router.push('/forum/article_edit/' + article.id)">
                        编辑
                    </el-button>
                    <el-popover :visible="visible" placement="top" :width="160">
                    <p>确定要删除这条内容吗？</p>
                <div style="text-align: right; margin: 0">
                    <el-button size="small" text @click="visible = false">取消</el-button>
                    <el-button size="small" type="danger" 
                            @click="sureToDeleteArticle">
                            确定
                    </el-button>
                </div>
                <template #reference>
                    <el-button @click="visible = true" type="danger" :icon="Delete">删除</el-button>
            </template>
        </el-popover>
                </div>
            </template>
        </el-page-header>
        <hr />
        <TextBlock :text="article" text-type="article"/>
        <el-row type="flex" justify="center" align="middle">
            <el-col :span="2"></el-col>
            <el-col :span="17">
                <div id="reply">
                    <el-input
                        v-model="reply"
                        :rows="4"
                        type="textarea"
                        maxlength="200"
                        show-word-limit
                        placeholder="发一条友善的回复"
                    />
                </div>
            </el-col>
            <el-col :span="1"></el-col>
            <el-col :span="3">
                <el-button id="sendButton" 
                        size="large"
                        type="primary"
                        @click="send">
                    发送
                </el-button>
            </el-col>
            <el-col :span="1"></el-col>
        </el-row>
        
        
        <!-- <el-divider content-position="left"></el-divider> -->
        <div style="margin:1em; position: relative;">
            <h3 style="display: inline-block;
                    height: 0.5em;
                    margin-right: 0.5em;"
                    id="comments">
                评论
            </h3>
            <el-text>{{ comments.length }}</el-text>
            <span style="position: absolute; top: 1em; right: 0">
                <el-button text @click="setOrderByTrend" :bg="orderByTrend" :style="{color: byTrend}">最热</el-button>
                <el-divider direction="vertical"></el-divider>
                <el-button text @click="setOrderByTime" :bg="!orderByTrend" :style="{color: byTime}">最新</el-button>
            </span>
        </div>
        
        <div v-if="orderByTrend">
            <div v-for="comment of commentsOrderByTrend" :key="comment.id">
                <TextBlock :text="comment" text-type="comment"  @click-delete="deleteComment(comment)"/>
            </div>
        </div>
        <div v-else>
            <div v-for="comment of commentsOrderByTime" :key="comment.id">
                <TextBlock :text="comment" text-type="comment"  @click-delete="deleteComment(comment)"/>
            </div>
        </div>
    </div>
</template>

<script setup>
    import{ ref, inject, reactive, computed } from 'vue';
    import { useRouter, useRoute } from "vue-router"
    const router = useRouter();
    const route = useRoute();
    import axios from 'axios';
    import request from '../../utils/request.js';
    import {Edit, Delete} from '@element-plus/icons-vue';
    import { success, error } from '../../utils/message.js'

    import TextBlock from './TextBlock.vue';
    
    const user = inject('user');

    const restServerSocket = inject("restServerSocket");
    const staticServerSocket = inject("staticServerSocket");

    const reply = ref('');

    const visible = ref(false);

    const articleId = route.params.article_id;
    const article = ref();
    article.value = await axios.get(restServerSocket + '/forum_read/article', {
        params: {
            'article_id': articleId,
            'uid': user.value?.uid
        }
    })  .then(resp => resp.data)
        .then(data => data.data);
    
    const sureToDeleteArticle = () => {
        request.deleteArticle(user.value.uid, articleId);
        success('已成功删除文章！3秒后自动跳转')
        visible.value = false;
        setTimeout(() => router.back(), 3000)
    }

    const updateVisible = computed(() => {
        return user?.value?.uid == article.value.uid;
    });
    
    const getComments = async () => {
        return await axios.get(restServerSocket + '/forum_read/comments', {
            params: { 'article_id': articleId, 'uid': user.value?.uid }
        })  .then(resp => resp.data)
        .then(data => data.data);
    };

    const comments = reactive([]);
    for (let comment of await getComments()) {
        comments.push(comment);
    }
    
    const orderByTrend = ref(true);
    const byTrend = computed( () => {
        if (orderByTrend.value) {
            return 'black';
        }
        return 'gray';
    });
    const byTime = computed( () => {
        if (!orderByTrend.value) {
            return 'black';
        }
        return 'gray';
    });

    const setOrderByTrend = (e) => {
        let target = e.target;
        if (target.nodeName == "SPAN"){
            target = e.target.parentNode;
        }
        target.blur();
        orderByTrend.value = true;
    }

    const setOrderByTime = (e) => {
        let target = e.target;
        if (target.nodeName == "SPAN"){
            target = e.target.parentNode;
        }
        target.blur();
        orderByTrend.value = false;
    }

    const baseComments = computed( () => {
        return comments.filter(e => e.comment_id == null);
    });

    const commentsOrderByTrend = computed( () => {
        return baseComments.value.toSorted((x, y) => (y.likes - y.hates) - (x.likes - x.hates));
    });

    const commentsOrderByTime = computed( () => {
        return baseComments.value.toSorted((x, y) => y.create_time.localeCompare(x.create_time));
    });

    const deleteComment = async (comment) => {
        return await axios.delete(restServerSocket + '/forum_update/comment', {
            params: {
                uid: user.value.uid,
                id: comment.id
            }
            }).then(resp => resp.data)
            .then(data => {
                if (data.status == 200) {
                    success("删除成功");
                    let length = comments.length;
                    for (let i = 0; i < length; i++) {
                        if (comments[i].id == comment.id) {
                            comments.splice(i, 1);
                            break;
                        }
                    }
                } else {
                    error(data.msg);
                }
            })
    }
    
    const send = async (e) => {
        let uid = user?.value?.uid;
        if (uid == null || uid == '') {
            error('请先登录！')
            return;
        }
        let target = e.target;
        if (target.nodeName == "SPAN"){
            target = e.target.parentNode;
        }
        target.blur();
        await axios.post(restServerSocket + '/forum_update/comment', {
            'article_id': articleId,
            'uid': user.value.uid,
            'nickname': user.value.nickname,
            'content': reply.value,
        })
            .then(resp => resp.data)
            .then(async data =>  {
                if (data.status == 200) {
                    success('发送成功！');
                    comments.push({
                        'uid': user.value.uid,
                        'nickname': user.value.nickname,
                        'content': reply.value,
                        'avatar': user.value.avatar,
                        likes: 0,
                        hates: 0,
                        liked: false,
                        hated: false,
                        create_time: timestampToTime(Date.now() / 1000)
                    });
                    reply.value = '';
                } else {
                    error(data.msg);
                }
            })
    }

    function timestampToTime(timestamp) {
        var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1):date.getMonth()+1) + '-';
        var D = (date.getDate()< 10 ? '0'+date.getDate():date.getDate())+ ' ';
        var h = (date.getHours() < 10 ? '0'+date.getHours():date.getHours())+ ':';
        var m = (date.getMinutes() < 10 ? '0'+date.getMinutes():date.getMinutes()) + ':';
        var s = date.getSeconds() < 10 ? '0'+date.getSeconds():date.getSeconds();
        return Y+M+D+h+m+s;
    }
</script>

<style scoped>
    #main {
        width: 60%;
        margin: auto;
        margin-top: 1em;
        border-style: solid;
        border-width: 1px;
        border-color: grey;
        border-radius: 1cap;
    }
    /* #reply {
        width: 85%;
        margin-left: 1em;
        margin-right: 1em;
    } */
    #sendButton {
        padding-top: 2.5em;
        padding-bottom: 2.5em;
        padding-left: 2.5em;
        padding-right: 2.5em;
    }
</style>