<script setup>
import axios from 'axios';
import { ref, reactive, inject, watch } from 'vue';
import { useRouter, useRoute } from "vue-router"
const restServerSocket = inject("restServerSocket");
const router = useRouter();
const route = useRoute();
const polling_id = route.params.polling_id;

import CaseResult from './CaseResult.vue';

const pollingMessage = ref({
    polling_id,
    polling_status: 'NOT_YET',
    time_interval_millis: 100,
    data: {}
});

const polling = () => {
    axios.get(restServerSocket + '/judge/result?polling_id=' + polling_id)
        .then(resp => resp.data)
        .then(data => data.data)
        .then(obj => pollingMessage.value = obj);
}

const intervalTag = ref(setInterval(polling, pollingMessage.value.time_interval_millis));

watch(() => pollingMessage.value.polling_status,
    (status) => {
        if (status == 'READY') {
            console.log("ready");
            clearInterval(intervalTag.value);
        }
    }
);

watch(() => pollingMessage.value.time_interval_millis,
    (time) => {
        if (time == null) {
            return;
        }
        clearInterval(intervalTag.value);
        intervalTag.value = setInterval(polling, time);
    }
);

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
    <div id="result">
        <h1>评测结果</h1>
        <div v-if="pollingMessage.polling_status=='READY'">
            <div v-if="pollingMessage.data.status=='AC'">
                <h1 style="color: rgb(16, 199, 16);">AC</h1>
            </div>
            <div v-else>
                <h1 style="color: rgb(235, 21, 5);">
                    {{ pollingMessage.data.status }}
                </h1>
            </div>
            <h3 style="display: inline;">通过样例：</h3>
            <p style="display: inline;">
                {{ 
                    pollingMessage.data.case_result.filter(e => e.status=='AC').length
                    + " / " + pollingMessage.data.case_result.length
                }}
            </p>
            <p></p>
            <h3 style="display: inline;">总时间花费: </h3>
            <p style="display: inline;">
                {{ 
                    pollingMessage.data.status == 'AC'
                    ? pollingMessage.data.time_millis +"ms"
                    : "N/A"
                }}
            </p>
            <p></p>
            <h3 style="display: inline;">总内存占用: </h3>
            <p style="display: inline;">
                {{ 
                    pollingMessage.data.status == 'AC'
                    ? formatSize(pollingMessage.data.memory)
                    : "N/A" 
                }}
            </p>
            <div></div>
            <div v-for="result of pollingMessage.data.case_result"
                    style="display: inline-block; margin: 0.5em;">
                <CaseResult 
                        :task_id="result.task_id"
                        :status="result.status"
                        :time_millis="result.time_millis"
                        :memory="result.memory"
                        >
                </CaseResult>
            </div>
        </div>
        <div v-else>
            <h3>等待中...</h3>
        </div>
    </div>
</template>

<style scoped>
#result {
    margin: auto;
    width: 80%;
    background-color: rgb(242, 242, 242);
    padding-left: 2em;
    padding-bottom: 1em;
    margin-top: 1em;
}
</style>