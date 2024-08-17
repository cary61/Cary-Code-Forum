<template>
    <div id="whole">
        <el-row type="flex" justify="center" align="middle">
            <el-col :span="15">
                <div class="mt-4">
                    <el-input v-model="keyword" :placeholder="'请输入' + selectBy" class="input-with-select">
                        <template #prepend>
                            <el-button :icon="Search" />
                        </template>
                        <template #append>
                            <el-button @click="search">
                                搜索
                            </el-button>
                        </template>
                    </el-input>
                </div>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="1">
                <el-text size="large">根据：</el-text>
            </el-col>
            <el-col :span="6">
                <el-radio-group v-model="selectBy">
                    <el-radio-button label="评论id" />
                    <el-radio-button label="评论内容" />
                </el-radio-group>
            </el-col>
        </el-row>

        <el-divider></el-divider>

        <div id="result">
            <div v-for="result of results" :key="result.uid">

                <el-row style="margin-bottom: 1em;" type="flex" justify="center" align="middle">
                    <el-col :span="15">
                        <ArticleTab :article="result" :show-comment-button="false"></ArticleTab>
                        <!-- <span>
                            <el-avatar class="demo-type" :src="staticServerSocket + result.avatar" size="large">
                            </el-avatar>
                        </span>
                        <el-divider direction="vertical" />
                        <h1 style="display: inline;">{{ result.nickname }}</h1>
                        <span style="margin-left: 1em; font-size: 0.9em; color: gray;">{{ "uid: " + result.uid }}</span>
                        <div class="center" style="padding-left: 4.2em; margin-top: 0.4em;">{{ result.signature }}</div> -->
                    </el-col>
                    <el-col :span="2"></el-col>
                    <el-col :span="2">
                        <el-popover :visible="banPannelVisivle" placement="top" :width="250">
                            <p>禁言直到：</p>
                            <div class="demo-datetime-picker"  style="margin-bottom: 0.8em;">
                                <div class="block">
                                    <span class="demonstration"></span>
                                    <el-date-picker v-model="datetime" type="datetime" placeholder="选择一个时间点"
                                        value-format="YYYY-MM-DD hh:mm:ss" />
                                </div>
                            </div>
                            <div style="text-align: right; margin: 0">
                                <el-button size="small" text @click="banPannelVisivle = false">取消</el-button>
                                <el-button size="small" type="warning"
                                    @click="banPannelVisivle = false; ban(result)">确定</el-button>
                            </div>
                            <template #reference>
                                <el-button @click="banPannelVisivle = true" type="warning">禁言此用户</el-button>
                            </template>
                        </el-popover>
                    </el-col>

                    <el-col :span="2">
                        <el-popover :visible="deletePannelVisible" placement="top" :width="160">
                            <p>真的要删除吗？此操作不可逆</p>
                            <div style="text-align: right; margin: 0">
                                <el-button size="small" text @click="deletePannelVisible = false">取消</el-button>
                                <el-button size="small" type="danger"
                                    @click="deletePannelVisible = false; deleteItem(result)">确定</el-button>
                            </div>
                            <template #reference>
                                <el-button @click="deletePannelVisible = true" type="danger">删除评论</el-button>
                            </template>
                        </el-popover>
                    </el-col>
                    <el-divider></el-divider>
                </el-row>
            </div>
        </div>

    </div>
</template>
<script setup>
import { reactive, ref, inject } from 'vue';
import axios from 'axios';
import { Search } from '@element-plus/icons-vue';
import { success, warning } from '../../utils/message.js';
import ArticleTab from '../../views/Forum/ArticleTab.vue'

const restServerSocket = inject("restServerSocket");
const staticServerSocket = inject("staticServerSocket");

const selectBy = ref('评论id');
const keyword = ref('');

const results = reactive([]);
const deletePannelVisible = ref(false);
const banPannelVisivle = ref(false);
const datetime = ref('')

const search = async () => {
    results.length = 0;
    console.log(results)
    if (selectBy.value == '评论id') {
        await searchById();
    } else if (selectBy.value == '评论内容') {
        await searchByContent();
    }
    if (results.length == 0) {
        warning("未搜索到匹配的结果");
    } else {
        success('搜索完成')
    }
}

const ban = (item) => {
    axios.post(restServerSocket + '/manage/user_ban', {
        uid: item.uid, 
        ban_until: datetime.value 
    }).then(resp => resp.data)
        .then(data => {
            if (data.status == 200) {
                success('禁言成功')
            } else {
                error('禁言失败')
            }
        })
}

const searchById = async () => {
    await axios.get(restServerSocket + '/manage/comment_by_id', {
        params: { 'id': keyword.value }
    }).then(resp => resp.data)
        .then(data => {
            for (let datum of data.data) {
                results.push(datum);
            }
        });
}


const searchByContent = async () => {
    await axios.get(restServerSocket + '/manage/comment_by_content', {
        params: { 'content': keyword.value }
    }).then(resp => resp.data)
        .then(data => {
            for (let datum of data.data) {
                results.push(datum);
            }
        });
}

const deleteItem = async (item) => {
    await axios.delete(restServerSocket + '/manage/comment', {
        params: { id: item.id }
    }).then(resp => resp.data)
        .then(data => {
            if (data.status == 200) {
                success('删除成功')
                for (let i = 0; i < results.length; i++) {
                    if (results[i].uid == item.uid) {
                        results.splice(i, 1);
                        break;
                    }
                }
            } else {
                error('删除失败')
            }
        })
}

</script>

<style scoped>
#whole {
    width: 80%;
    margin: auto;
    margin-top: 1em;
}
</style>