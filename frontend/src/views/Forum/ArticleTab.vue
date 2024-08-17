<template>
    <div class="main">
        <div @click="$router.push('/user_index/' + article.name);"
                v-show="showAuthor"
                style="cursor: pointer; display: inline-block;">
            <el-avatar :src="staticServerSocket + article.avatar"></el-avatar>
            <h5 style="display: inline; margin-left: 0.5em;">{{ article.nickname }}</h5>
        </div>
        <div @click="$router.push('/forum/article/' + article.id);"
                style="cursor: pointer;">
            <h4 id="title" style="margin-left: 0.5em;">
                {{ article.title }}
            </h4>
            <div>
            <el-text line-clamp="3" id="content" style="margin-left: 0.5em;">
                {{ article.content }}
            </el-text>
        </div>
        </div>
        <span id="icons">
            <el-button round @click="likeClick">
                <img :src="likeButtonIcon" height="20" style="margin-right: 0.5em;">
                {{ article.likes }}
            </el-button>
            <el-button round @click="hateClick">
                <img :src="hateButtonIcon" height="20" style="margin-right: 0.5em;">
                {{ article.hates }}
            </el-button>
            <el-button round @click="$router.push('/forum/article/' + article.id)" v-if="showCommentButton">
                <img :src="staticServerSocket + '/svg/' + 'comment.svg'" height="18" style="margin-right: 0.5em;">
                {{ article.comments_amount }}
            </el-button>
        </span>
    </div>
    <!-- {{ article }} -->
    <el-divider></el-divider>
</template>

<script setup>
    import{ computed, inject } from 'vue';
    import axios from 'axios';
    import request from '../../utils/request.js';
    import { MagicStick, ChatLineRound } from '@element-plus/icons-vue'

    const restServerSocket = inject("restServerSocket");
    const staticServerSocket = inject("staticServerSocket");
    const props = defineProps({
        article: Object,
        showAuthor: {
            type: Boolean,
            default: true,
            require: false
        },
        showCommentButton: {
            type: Boolean,
            default: true,
            require: false
        },
    });
    const user = inject('user');

    const article = props.article;

    const likeButtonIcon = computed( () => {
        return staticServerSocket + '/svg/' + (article.liked ? "appreciate_fill_light.svg" : "appreciate_light.svg");
    });

    const hateButtonIcon = computed( () => {
        return staticServerSocket + '/svg/' + (article.hated ? "oppose_fill_light.svg" : "oppose_light.svg");
    });
    
    const likeClick = async (e) => {
        let target = e.target;
        if (target.nodeName == "SPAN"){
            target = e.target.parentNode;
        }
        target.blur();
        let liked = article.liked;
        article.liked = !article.liked;
        let res;
        if (liked) {
            res = await request.act('unlike', user.value.uid, article.id, 'article');
        } else {
            if (article.hated) {
                await hateClick(e);
            }
            res = await request.like(user.value.uid, article.id, 'article');
        }
        if (res) {
            if (liked) {
                article.likes--;
            } else {
                article.likes++;
            }
            
        }
    }

    const hateClick = async (e) => {
        let target = e.target;
        if (target.nodeName == "SPAN"){
            target = e.target.parentNode;
        }
        target.blur();
        let hated = article.hated;
        article.hated = !article.hated;
        let res;
        if (hated) {
            res = await request.act('unhate', user.value.uid, article.id, 'article');
        } else {
            if (article.liked) {
                await likeClick(e);
            }
            res = await request.hate(user.value.uid, article.id, 'article');
        }
        if (res) {
            if (hated) {
                article.hates--;
            } else {
                article.hates++;
            }
        }
    }
</script>

<style scoped>
    .main {
        /* border-style: ridge; */
        position: relative;
        padding-left: 1em;
        padding-right: 1em;
    }
    #content {
        max-height: 4em;
        margin-bottom: 0.8em;
    }
    #icons {
        position: relative;
        bottom: 0;
    }
</style>