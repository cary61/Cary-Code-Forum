<template>
    <div>
        <div class="center" style="position: relative; margin-top: 3em;">
            <span>
                <el-avatar class="demo-type"
                        :src="staticServerSocket + indexUser.avatar"
                        size="large">
                </el-avatar>
            </span>
            <el-divider direction="vertical" />
            <h1 style="display: inline;">{{ indexUser.nickname }}</h1>
            <span style="margin-left: 0.5em; font-size: 0.9em; color: gray;">{{"@" + indexUser.name }}</span>
            <span style="position: absolute; right: 1em;">
                <el-button type="primary" v-show="unableToUpdate" @click="updateFormVisivle = true">修改信息</el-button>
            </span>

            <el-dialog v-model="updateFormVisivle" title="信息修改" width="60%" @keyup.enter="updateInfo">
        <el-form :model="updatingInfo">
            <el-form-item label="用户名" :label-width="formLabelWidth">
                <el-input v-model="updatingInfo.name" autocomplete="off" disabled />
            </el-form-item>
            <el-form-item label="昵称" :label-width="formLabelWidth">
                <el-input v-model="updatingInfo.nickname" autocomplete="off" />
            </el-form-item>
            <el-form-item label="签名" :label-width="formLabelWidth">
                <el-input v-model="updatingInfo.signature" autocomplete="off" />
            </el-form-item>
            <el-form-item label="头像" :label-width="formLabelWidth">
                <!-- <el-avatar class="demo-type"
                        :src="staticServerSocket + updatingInfo.avatar"
                        size="large">
                </el-avatar> -->
                <img :src="staticServerSocket + updatingInfo.avatar"
                        style="width: 15em;">
            </el-form-item>
        </el-form>
        <Uploader style="width: 80%; margin: auto;"
                @file-path-return="pictureUpload"
        ></Uploader>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="updateFormVisivle = false">取消</el-button>
                <el-button type="primary" @click="updateInfo">修改</el-button>
            </span>
        </template>
    </el-dialog>
            
        </div>
        <div class="center" style="padding-left: 2em;">{{ indexUser.signature }}</div>
        
        <!-- <el-divider class="center" /> -->

        <el-collapse class="center"
                v-model="pannel">
            <el-collapse-item name="帖子">
                <template #title>
                    <h2>帖子</h2>
                </template>
                <div v-for="article in articles" :key="article.id">
                    <ArticleTab :article="article" :show-author="false"></ArticleTab>
                </div>
            </el-collapse-item>
            
            <el-collapse-item name="最近点赞">
                <template #title>
                    <h2>最近点赞</h2>
                </template>
                <div v-for="article in recentLikedArticles" :key="article.id">
                    <ArticleTab :article="article"></ArticleTab>
                </div>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script setup>
    import { ref, computed, inject, reactive } from 'vue';
    import { useRouter, useRoute } from "vue-router"
    import axios from 'axios';
    const router = useRouter();
    const route = useRoute();
    import Uploder from '../../components/Uploader.vue'
    import { error, success } from '../../utils/message.js'
    import ArticleTab from '../Forum/ArticleTab.vue';
    import Uploader from '../../components/Uploader.vue';

    const restServerSocket = inject("restServerSocket");
    const staticServerSocket = inject("staticServerSocket");

    let indexUser = ref({});

    const userName = computed(() => {
        return route.params.user_name;
    });
    let user = inject('user');
    const unableToUpdate = computed(() => {
        return user.value.name == route.params.user_name;
    });
    const getIndexResp = async () => {
        let indexResp = await fetch(restServerSocket + '/user/by_name?name=' + route.params.user_name)
        .then(resp => resp.json());
        if (indexResp.status != 200) {
            error('获取用户信息失败');
            indexUser.value = null;
        } else {
            indexUser.value = indexResp.data;
            console.log(indexUser.value);
        }
    }
    await getIndexResp();

    const articles = reactive([]);
    await axios.get(restServerSocket + "/forum_read/article_list_by_name", {
        params: {'name': userName.value}
     }) .then(resp => resp.data)
        .then(data => {
            data.data.forEach(e => articles.push(e));
    });
    
    const recentLikedArticles = reactive([]);
    await axios.get(restServerSocket + "/forum_read/recent_liked_articles", {
        params: {'name': userName.value}
     }) .then(resp => resp.data)
        .then(data => {
            data.data.forEach(e => recentLikedArticles.push(e));
    });

    const pannel = ref(['帖子']);

    const updatingInfo = reactive({
        name: indexUser.value.name,
        nickname: indexUser.value.nickname,
        signature: indexUser.value.signature,
        avatar: indexUser.value.avatar,
        uid: indexUser.value.uid
    })

    const formLabelWidth = '10em'

    const pictureUpload = (file_path) => {
        updatingInfo.avatar = file_path;
        console.log(updatingInfo.avatar)
    }

    const updateFormVisivle = ref(false);
    const updateInfo = async (e) => {
        let target = e.target;
        if (target.nodeName == "SPAN"){
            target = e.target.parentNode;
        }
        target.blur();
        return await axios.put(restServerSocket + '/user', updatingInfo)
            .then(resp => resp.data)
            .then(data => {
                if (data.status == 200) {
                    success('更新成功！')
                    indexUser.value.nickname = updatingInfo.nickname;
                    indexUser.value.signature = updatingInfo.signature;
                    indexUser.value.avatar = updatingInfo.avatar;
                    updateFormVisivle.value = false;
                } else {
                    error('更新失败')
                }
            })
    }

</script>

<style scoped>
    .center {
        position: relative;
        width: 80%;
        margin: auto;
        margin-top: 1em;
    }
</style>