<script setup>
import { useRouter, useRoute } from "vue-router"
const router = useRouter();

const props = defineProps({
    id: Number,
    name: String,
    difficulty: String
});
const click = () => {
    router.push('/problem_list/' + props.id);
}
</script>

<template>
    <div id="problemtab" @click="click">
        <el-row type="flex" 
                justify="space-around" 
                align="middle">
            <el-col :span="2">
                {{ id }}
            </el-col>
            <el-col :span="10">
                {{ name }}
            </el-col>
            <el-col :span="6">
                {{ difficulty }}
            </el-col>
        </el-row>
    </div>
</template>

<style scoped>
#problemtab {
    margin-top: 0.5em;
    border-style: solid;
    border-width: 1px;
    border-color: grey;
    border-radius: 1cap;
    min-height: 4em;
    line-height: 4em;
    text-align: center;
    cursor: pointer;
}
</style>