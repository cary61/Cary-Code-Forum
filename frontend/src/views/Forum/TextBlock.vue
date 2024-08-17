<template>
    <div id="block">
        <el-row>
            <el-col :span="3" id="author">
                <div style="width: 99%; margin: auto; ">
                    <el-row type="flex" justify="center" align="middle">
                        <el-avatar class="demo-type" :src="staticServerSocket + text.avatar" size="large">
                        </el-avatar>
                    </el-row>
                    <el-row type="flex" justify="center" align="middle">
                        <el-text>{{ text.nickname }}</el-text>
                    </el-row>
                </div>
            </el-col>
            <el-col :span="1">
                <el-divider direction="vertical"></el-divider>
            </el-col>
            <el-col :span="20" style="display: relative;">
                <el-row>
                    <el-col id="content">
                        <el-text size="large">{{ text.content }}</el-text>
                    </el-col>
                </el-row>
            </el-col>
        </el-row>
        <el-row v-if="textType=='comment'">
            <el-col :span="4"></el-col>
            <el-col :span="10">
                <el-button round 
                        style="margin-right: 0.5em;" 
                        @click="likeClick">
                    <img :src="likeButtonIcon" height="20" style="margin-right: 0.5em;">
                    {{ text.likes }}
                </el-button>
                <el-button round 
                        @click="hateClick">
                    <img :src="hateButtonIcon" height="20" style="margin-right: 0.5em;">
                    {{ text.hates }}
                </el-button>
                <!-- {{ liked }}___{{ hated }} -->
            </el-col>
            <el-col :span="10">
                <el-button
                        v-if="user.uid==text.uid"
                        size="small"  
                        text 
                        style="float: right;"
                        @click="$emit('clickDelete')">
                    删除
                </el-button>
            </el-col>
        </el-row>
        <el-row v-if="textType=='article'" type="flex" justify="space-evenly" align="middle"
                style="margin: 1em;">
            <el-col :span="3">
                <el-button circle
                        size="large"
                        style="margin-right: 0.5em;" 
                        @click="likeClick">
                    <img :src="likeButtonIcon" height="30" style="margin-right: 0.5em;">
                    {{ text.likes }}
                </el-button>
            </el-col>
            <el-col :span="3">
                <el-button circle 
                        size="large"
                        @click="hateClick">
                    <img :src="hateButtonIcon" height="30" style="margin-right: 0.5em;">
                    {{ text.hates }}
                </el-button>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24">
                <el-text size="small" style="float: right; margin-right: 1em;">
                    发表于：{{ text.create_time }}
                </el-text>
                <div clear:both></div>
                <el-text size="small" style="float: right; margin-right: 1em;"  v-if="textType=='article' && text.create_time != text.update_time">
                    最后修改于：{{ text.update_time }}
                </el-text>
                <div clear:both></div>
            </el-col>
        </el-row>
        
    </div>
    
    
    
    <el-divider></el-divider>
</template>

<script setup>
    import { ref, inject, computed, reactive } from 'vue';
    import { MagicStick, ChatLineRound } from '@element-plus/icons-vue'
    const props = defineProps(['text', 'textType']);
    import axios from 'axios';
    import request from '../../utils/request';
    const staticServerSocket = inject("staticServerSocket");
    const user = inject('user');

    defineEmits(['clickDelete']);

    const likeButtonIcon = computed( () => {
        return staticServerSocket + '/svg/' + (props.text.liked ? "appreciate_fill_light.svg" : "appreciate_light.svg");
    });

    const hateButtonIcon = computed( () => {
        return staticServerSocket + '/svg/' + (props.text.hated ? "oppose_fill_light.svg" : "oppose_light.svg");
    });

    const likeClick = async (e) => {
        let target = e.target;
        if (target.nodeName == "SPAN"){
            target = e.target.parentNode;
        }
        target.blur();
        let liked = props.text.liked;
        props.text.liked = !props.text.liked;
        let res;
        if (liked) {
            res = await request.act('unlike', user.value.uid, props.text.id, props.textType);
        } else {
            if (props.text.hated) {
                hateClick(e);
            }
            res = await request.like(user.value.uid, props.text.id, props.textType);
        }
        if (res) {
            if (liked) {
                props.text.likes--;
            } else {
                props.text.likes++;
            }
        }
    }

    const hateClick = async (e) => {
        let target = e.target;
        if (target.nodeName == "SPAN"){
            target = e.target.parentNode;
        }
        target.blur();
        let hated = props.text.hated;
        props.text.hated = !props.text.hated;
        let res;
        if (hated) {
            res = await request.act('unhate', user.value.uid, props.text.id, props.textType);
        } else {
            if (props.text.liked) {
                likeClick(e);
            }
            res = await request.hate(user.value.uid, props.text.id, props.textType);
        }
        if (res) {
            if (hated) {
                props.text.hates--;
            } else {
                props.text.hates++;
            }
            
        }
    }
</script>

<style scoped>
    #block {
        width: 98%;
        margin: auto;
    }

    /* #author {
            background-color: rgb(250, 250, 250);
        } */
    #content {
        width: 80%;
        padding-right: 0.5em;
    }

    .el-divider--vertical {
        display: inline-block;
        width: 1px;
        height: 100%;
        margin: 0 8px;
        vertical-align: middle;
        position: relative;
    }
    .el-button.el-button--large.is-circle {
        min-width: 4em;
        min-height: 4em;
    }
</style>