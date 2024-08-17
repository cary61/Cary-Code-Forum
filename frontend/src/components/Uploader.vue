<template>
    <el-upload class="upload-demo"
            drag 
            :action="getUploadAddress()"
            name="multipartFile"
            :on-success="onSuccess"
            :show-file-list="showFileList"
            @on-success="onSuccess">
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
            将文件拖拽到此处 或 <em>点击上传</em>
        </div>
        <template #tip>
            <div class="el-upload__tip">
                请上传小于10M的图片
            </div>
        </template>
    </el-upload>
</template>
  
<script setup>
import { UploadFilled } from '@element-plus/icons-vue'
import { inject } from 'vue';
import {success} from '../utils/message.js'

const emit = defineEmits(['filePathReturn']);
const prop = defineProps({
    showFileList : {
        type: Boolean,
        default: false
    }
});

const restServerSocket = inject("restServerSocket");
const staticServerSocket = inject("staticServerSocket");

const getUploadAddress = () => {
    return restServerSocket + '/file';
}

const onSuccess = (resp, file, fileList) => {
    success('图片上传成功！');
    emit('filePathReturn', resp.data.file_path)
}

</script>
  