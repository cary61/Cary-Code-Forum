<script setup>
const props = defineProps([
    'task_id',
    'status',
    'time_millis',
    'memory'
]);
const kb = 1024, mb = 1024 * kb, gb = 1024 * mb, tb = 1024 * gb;
const formatSize = (size) => {
    if (size < kb) {
        return size + "B";
    } else if (size < mb) {
        return (size / kb).toFixed(2) + "KB";
    } else if (size < gb) {
        return (size / mb).toFixed(2) + "MB";
    } else if (size < tb) {
        return (size / gb).toFixed(2) + "GB";
    } else {
        return (size / tb).toFixed(2) + "TB";
    }
}
</script>

<template>
    <div id="caseResult">
        <p>{{ task_id }}</p>
        <div v-if="status=='AC'">
            <p style="color: rgb(16, 199, 16);">AC</p>
        </div>
        <div v-else>
            <p style="color: rgb(235, 21, 5);">
                {{ status }}
            </p>
        </div>
        <div>
            {{ 
                status == 'AC'
                ? time_millis + "ms" 
                : "N/A"
            }}
        </div>
        <div>
            {{ 
                status == 'AC'
                ? formatSize(memory) 
                : "N/A"
            }}
        </div>
    </div>
</template>

<style scoped>
#caseResult {
    margin-top: 0.5em;
    padding: 1em;
    border-style: solid;
    border-width: 1px;
    border-color: grey;
    border-radius: 1cap;
    min-height: 4em;
    min-width: 6em;
    line-height: 1em;
    text-align: center;
}
</style>