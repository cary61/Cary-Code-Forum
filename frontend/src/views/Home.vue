<template>
    <div id="master">
        <img :src="staticServerSocket + '/public/carycode-large.png'"
                style="width: 70em;">
        <!-- <el-text>梦想在这里启航</el-text> -->
        <!-- <h3>最近热帖</h3> -->
        <el-divider></el-divider>
    </div>
</template>

<script setup>
import { inject } from 'vue';
const restServerSocket = inject("restServerSocket");
const staticServerSocket = inject("staticServerSocket");
</script>

<style scoped>
#master {
    width: 70%;
    margin: auto;
}
</style>