<script setup>
import axios from 'axios';
import { ref, reactive, inject } from 'vue';
import { useRouter, useRoute } from "vue-router"
const restServerSocket = inject("restServerSocket");
const router = useRouter();
const route = useRoute();

import { error } from '../../utils/message';

const problem_id = route.params.problem_id;
const problem = reactive({
    id: problem_id,
    name: '',
    background: '',
    description: '',
    inputFormat: '',
    outputFormat: '',
    tip: '',
    difficulty: ''
});

axios.get(restServerSocket + '/judge/problem?problem_id=' + problem_id)
        .then(resp => resp.data)
        .then(data => data.data)
        .then(e => {
            problem.id = e.id;
            problem.name = e.name;
            problem.background = e.background;
            problem.description = e.description;
            problem.inputFormat = e.input_format;
            problem.outputFormat = e.output_format;
            problem.tip = e.tip;
            problem.difficulty = e.difficulty;
        });
const dialogVisivle = ref(false);
const formLabelWidth = '10em';

const sourceCode = reactive({
    language: 'JAVA',
    code: ''
});
const commitCode = () => {
    dialogVisivle.value = false;
    axios.post(restServerSocket + '/judge/source_code', {
        code: sourceCode.code,
        language: sourceCode.language,
        problem_id
    })
        .then(resp => resp.data)
        .then(data => {
            if (data.status != 200) {
                error('上传失败');
            } else {
                let pollingId = data.data.polling_id;
                let timeInterval = data.data.time_interval_millis;
                setTimeout(() => router.push('/problem_list/result/' + pollingId)
                , timeInterval);
            }
        });
}
const click = (e) => {
    let target = e.target;
    if (target.nodeName == "SPAN"){
        target = e.target.parentNode;
    }
    target.blur();
    dialogVisivle.value = true;
}
const languageOptions = [
  {
    value: 'JAVA',
    label: 'Java',
  },
  {
    value: 'PYTHON',
    label: 'Python',
  },
];

</script>


<template>
    <div id="problem">
        <div style="float: right; 
                margin-right: 1em;
                margin-top: 1em;">
            {{ problem.difficulty }}
        </div>
        <h1>{{ problem.id + '. ' + problem.name }}</h1>
        <div v-if="problem.background">
            <h3>题目背景</h3>
            <p>{{ problem.background }}</p>
        </div>
        <h3>题目描述</h3>
        <p>{{ problem.description }}</p>
        <h3>输入格式</h3>
        <p>{{ problem.inputFormat }}</p>
        <h3>输出格式</h3>
        <p>{{ problem.outputFormat }}</p>
        <div v-if="problem.tip">
            <h3>说明/提示</h3>
            <p>{{ problem.tip }}</p>
        </div>
        <el-button type="primary" @click="click">提交代码</el-button>
    </div>

    <el-dialog v-model="dialogVisivle" title="提交代码" width="60%">
        <el-form :model="sourceCode">
            <el-form-item label="语言" :label-width="formLabelWidth">
                <el-select v-model="sourceCode.language" class="m-2" placeholder="Select">
                    <el-option
                        v-for="item in languageOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="源代码" :label-width="formLabelWidth">
                <el-input
                    v-model="sourceCode.code"
                    :autosize="{minRows: 15, maxRows: 20}"
                    type="textarea"
                    placeholder="请输入源代码"
                />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisivle = false">取消</el-button>
                <el-button type="primary" @click="commitCode">提交</el-button>
            </span>
        </template>
    </el-dialog>
</template>


<style scoped>
#problem {
    margin: auto;
    width: 80%;
    background-color: rgb(242, 242, 242);
    padding-left: 2em;
    padding-bottom: 1em;
    margin-top: 1em;
}
</style>