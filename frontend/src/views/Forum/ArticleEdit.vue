<template>
    <div id="main">
        <el-page-header @back="$router.back()"
                style="padding: 1em 1em 0.5em 1em;">
            <template #content>
                <el-text style="padding-right: 1em;">分区</el-text>
                <el-cascader v-model="defaultBlock" 
                        :options="blocks"
                        @change="handleBlockChange"
                        style="width: 70%;"/>
            </template>
            <el-input v-model="articleForm.title"
                    placeholder="请输入标题"
                    clearable/>
            <template #extra>
                <div class="flex items-center">
                    <el-button @click="sendClick" type="primary" class="ml-2">{{ isNewArticle ? '发布' : '保存' }}</el-button>
                    <el-button @click="$router.back()" class="ml-2">取消</el-button>
                </div>
            </template>
        </el-page-header>
        <div style="margin: 1em;">
            <el-text>内容</el-text>
            <el-input v-model="articleForm.content"
                placeholder="内容写在这(*^_^*)"
                :autosize="{ minRows: 7, maxRows: 100 }"
                type="textarea"
                maxlength="1000"
                show-word-limit>
            </el-input>
        </div>
    </div>
</template>

<script setup>
    import { reactive, ref, inject } from 'vue';
    import { useRouter, useRoute } from "vue-router"
    import axios from 'axios';
    import { success, error } from '../../utils/message.js';
    const router = useRouter();
    const route = useRoute();
    const restServerSocket = inject("restServerSocket");
    const staticServerSocket = inject("staticServerSocket");

    const user = inject('user');

    const articleForm = reactive({
        id: null,
        title: '',
        content: '',
        type: '',
        uid: ''
    });
    const isNewArticle = ref(route.params?.article_id == null);
    console.log('isNewArticle: ' + isNewArticle.value)
    if (route.params.article_id != null) {
        await axios.get(restServerSocket + '/forum_read/article', {
            params: {
                article_id: route.params.article_id,
            }
            }).then(resp => resp.data.data)
            .then(data => {
                articleForm.id = data.id;
                articleForm.title = data.title;
                articleForm.content = data.content;
                articleForm.type = data.type;
                articleForm.uid = data.uid;
            })
    }

    const sendClick = async () => {
        if (isNewArticle.value) {
            await addArticle();
        } else {
            await updateArticle();
        }
    }

    const addArticle = async () => {
        return await axios.post(restServerSocket + '/forum_update/article',
            {
                title: articleForm.title,
                content: articleForm.content,
                type: articleForm.type,
                uid: user.value.uid
            }
        )  .then(resp => resp.data)
            .then(data => {
                if (data.status == 200) {
                    success('帖子发布成功！3秒后自动跳转');
                    setTimeout(() => router.back(), 3000);
                } else {
                    error(data.msg)
                }
            });
    }

    const updateArticle = async () => {
        return await axios.put(restServerSocket + '/forum_update/article', {
            id: articleForm.id,
            title: articleForm.title,
            content: articleForm.content,
            type: articleForm.type,
            uid: user.value.uid
        })  .then(resp => resp.data)
            .then(data => {
                if (data.status == 200) {
                    success('帖子更新成功！3秒后自动跳转');
                    setTimeout(() => router.back(), 3000);
                } else {
                    error(data.msg)
                }
        });
    }




    let type = isNewArticle ? '求助' : articleForm.type;
    const defaultBlock = ref([type])
    const blocks = [
        {
            label: '求助',
            value: '求助'
        },
        {
            label: '经验',
            value: '经验'
        },
        {
            label: '杂谈',
            value: '杂谈'
        },
        {
            label: '记录',
            value: '记录'
        },
    ];
    const handleBlockChange = (block) => {
        articleForm.type = block[0];
    }
</script>

<style>
    #main {
        width: 90%;
        margin: auto;
        margin-top: 1em;
        border-style: solid;
        border-width: 1px;
        border-color: grey;
        border-radius: 1cap;
    }
</style>