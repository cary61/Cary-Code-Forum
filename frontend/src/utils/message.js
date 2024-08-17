import { ElMessage } from "element-plus";

const success = (str) => {
    ElMessage({
        showClose: true,
        message: str,
        type: 'success',
    });
}

const error = (str) => {
    ElMessage({
        showClose: true,
        message: str,
        type: 'error',
    });
}

const warning = (str) => {
    ElMessage({
        showClose: true,
        message: str,
        type: 'warning',
    });
}

export {success, error, warning};