<template>
    <div style="display: relative;">
        <!-- <h3 class="center" style="text-align: center;">帖子分区</h3> -->
        <div class="center" style="display: flex;">
            <el-button size="large" style="flex:1" plain class="tab" type="primary" @click="changeFilter">求助</el-button>
            <el-button size="large" style="flex:1" plain class="tab" type="primary" @click="changeFilter" >经验</el-button>
            <el-button size="large" style="flex:1" plain class="tab" type="primary" @click="changeFilter" >杂谈</el-button>
            <el-button size="large" style="flex:1" plain class="tab" type="primary" @click="changeFilter" >记录</el-button>
        </div>
        <div style="text-align: right; padding-right: 1em;">
            <el-text style="">显示：{{ showArticleType }}</el-text>
        </div>
        
        <el-divider  class="center" />
        <div class="center">
            <div v-for="article of showArticleList" :key="article.id">
                <ArticleTab :article="article"></ArticleTab>
            </div>
        </div>
        <el-button type="primary" 
                size="large" 
                :icon="Plus" 
                round
                style="position: fixed; right: 4.5em; top: 50em"
                :disabled="!isLoggedIn"
                @click="$router.push('forum/article_edit')"
                >发布帖子
        </el-button>
    </div>
</template>

<script setup>
    import { ref, reactive, computed, inject } from 'vue';
    import { useRouter, useRoute } from "vue-router"
    const router = useRouter();
    const route = useRoute();
    import axios from 'axios';
    import { Plus } from '@element-plus/icons-vue'
    import ArticleTab from './ArticleTab.vue';
    import { error } from '../../utils/message.js'
    
    const restServerSocket = inject("restServerSocket");
    const staticServerSocket = inject("staticServerSocket");
    
    const user = inject('user');

    let isLoggedIn = inject('isLoggedIn');
    const articles = reactive([]);
    await axios.get(restServerSocket + "/forum_read/article_list", {
        params: {'uid': user.value?.uid}
     }) .then(resp => {console.log(user.value?.uid);; return resp.data})
        .then(data => {
            // articles = data.data;
            data.data.forEach(e => articles.push(e));
        });

    const showArticleType = ref('全部');
    const showArticleList = computed( () => {
        if (showArticleType.value == '全部') {
            return articles;
        }
        return articles.filter(e => e.type == showArticleType.value);
    });

    const changeFilter = (e) => {
        let target = e.target;
        if (target.nodeName == "SPAN"){
            target = e.target.parentNode;
        }
        target.blur();
        let newShowArticleType = e.target.innerText;
        if (showArticleType.value == newShowArticleType) {
            showArticleType.value = '全部';
        } else {
            showArticleType.value = newShowArticleType;
        }
    }
    
</script>

<style scoped>
    .center {
        position: relative;
        width: 60%;
        margin: auto;
        margin-top: 1em;
    }
    .tab{
        margin: auto;
        width: 5em;
        padding: 1em;
    }
</style>