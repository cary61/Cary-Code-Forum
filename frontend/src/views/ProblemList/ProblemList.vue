<script setup>
import { ref, reactive, inject } from 'vue';
import axios from 'axios';
import ProblemTab from './ProblemTab.vue';
const problems = reactive([]);
const restServerSocket = inject("restServerSocket");
axios.get(restServerSocket + '/judge/problem_list')
        .then(resp => resp.data)
        .then(data => data.data)
        .then(array => array.forEach(e => problems.push(e)));

</script>


<template>
    <div id="master">
        <el-row type="flex" 
                justify="space-around" 
                align="middle">
            <el-col :span="2">
                序号
            </el-col>
            <el-col :span="10">
                题目
            </el-col>
            <el-col :span="6">
                难度
            </el-col>
        </el-row>
        <div v-for="problem in problems">
            <ProblemTab :id="problem.id" 
                        :name="problem.name" 
                        :difficulty="problem.difficulty">
            </ProblemTab>
        </div>
    </div>
</template>


<style scoped>
#master {
    margin: auto;
    width: 80%;
    min-height: 2em;
    line-height: 2em;
    text-align: center;
}
</style>