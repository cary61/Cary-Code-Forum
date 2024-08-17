<template>
    <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect" router>
        <el-menu-item index="/">
            <img style="width: 100px" src="/CaryCode-logo.png" alt="Element logo" />
        </el-menu-item>
        <div class="flex-grow" />
        <!-- <el-menu-item index="/">主页</el-menu-item> -->
        <el-menu-item index="/tutorial">学习</el-menu-item>
        <el-menu-item index="/forum">论坛</el-menu-item>
        <el-menu-item index="/problem_list">题库</el-menu-item>
        <!-- <el-menu-item index="/about">关于我们</el-menu-item> -->
        <el-sub-menu index="/visualization">
            <template #title>
                可视化
            </template>
            <el-menu-item @click="gotoGraphVisualization">
                寻路算法
            </el-menu-item>
            <el-menu-item @click="gotoSortingVisualization">
                排序算法
            </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="manage" v-if="userIsManager">
            <template #title>
                网站管理
            </template>
            <el-menu-item index="/manage/user">
                用户管理
            </el-menu-item>
            <el-menu-item index="/manage/article">
                帖子管理
            </el-menu-item>
            <el-menu-item index="/manage/comment">
                评论管理
            </el-menu-item>
            <el-menu-item index="/manage/problem">
                题目管理
            </el-menu-item>
        </el-sub-menu>

        <div class="flex-grow" />
        <el-menu-item v-if="!isLoggedIn" @click="loginFormVisible = true">登录</el-menu-item>
        <el-menu-item v-if="!isLoggedIn" @click="registerFormVisible = true">注册</el-menu-item>
        
        <el-sub-menu v-if="isLoggedIn" index="user">
            <template #title>
                <el-avatar class="demo-type" :src="staticServerSocket + user?.avatar"></el-avatar>
            </template>
            <el-menu-item @click="toUserIndex()">个人主页</el-menu-item>
            <el-menu-item @click="logout()">退出登录</el-menu-item>
        </el-sub-menu>
    </el-menu>

    <el-dialog v-model="registerFormVisible" title="用户注册" width="30%" @keyup.enter="register">
        <el-form :model="registerForm">
            <el-form-item label="用户名" :label-width="formLabelWidth">
                <el-input v-model="registerForm.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="昵称" :label-width="formLabelWidth">
                <el-input v-model="registerForm.nickname" autocomplete="off" />
            </el-form-item>
            <el-form-item label="密码" :label-width="formLabelWidth">
                <el-input v-model="registerForm.password" autocomplete="off" type="password" />
            </el-form-item>
            <el-form-item label="确认密码" :label-width="formLabelWidth">
                <el-input v-model="registerForm.repeatedPassword" autocomplete="off" type="password" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="registerFormVisible = false">取消</el-button>
                <el-button type="primary" @click="register()">注册</el-button>
            </span>
        </template>
    </el-dialog>

    <el-dialog v-model="loginFormVisible" title="用户登录" width="30%" @keyup.enter="login">
        <el-form :model="loginForm">
            <el-form-item label="用户名" :label-width="formLabelWidth">
                <el-input v-model="loginForm.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="密码" :label-width="formLabelWidth">
                <el-input v-model="loginForm.password" autocomplete="off" type="password" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="loginFormVisible = false">取消</el-button>
                <el-button type="primary" @click="login()">登录</el-button>
            </span>
        </template>
    </el-dialog>
</template>
  
<script setup>
import { ref, inject, computed } from 'vue';
import { useRouter, useRoute } from "vue-router"
const router = useRouter();
const route = useRoute();

import Cookie from 'js-cookie';
import { success, error } from '../utils/message';

const restServerSocket = inject("restServerSocket");
const staticServerSocket = inject("staticServerSocket");
const isLoggedIn = inject('isLoggedIn');
const user = inject('user');

const token = ref('');
async function refreshLoginState() {
    token.value = Cookie.get('token');
    if (token == null) {
        isLoggedIn.value = false;
        clear(user.value);
        return;
    }
    user.value = await fetch(restServerSocket + '/user/by_token', {
        method: "GET",
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors'
    }).then(resp => resp.json())
        .then(json => json.data);
    if (user?.value?.name != null) {
        isLoggedIn.value = true;
    } else {
        isLoggedIn.value = false;
    }
}

await refreshLoginState();

const gotoGraphVisualization = () => {
    location.href='./pages/graph/graph.html';
}

const gotoSortingVisualization = () => {
    location.href='./pages/sorting/sorting_page.html';
}

const formLabelWidth = '10em'
let registerFormVisible = ref(false);
let registerForm = ref({
    name: '',
    nickname: '',
    password: '',
    repeatedPassword: ''
});

const userIsManager = computed(() => {
    if (user == null) {
        return false;
    }
    if (user.value == null) {
        return false;
    }
    return user.value.authority=='ADMINISTRATOR';
});

let register = async () => {
    console.log("点击注册")
    if (registerForm.value.name.length < 6) {
        error('用户名至少需要6个字符');
    } else if (registerForm.value.password != registerForm.value.repeatedPassword) {
        error('密码两次输入不一致');
    } else if (registerForm.value.password.length < 8) {
        error('密码至少需要8个字符');
    } else {
        let json = await fetch(restServerSocket + "/user/register", {
            method: "POST",
            body: JSON.stringify({
                'name': registerForm.value.name,
                'nickname': registerForm.value.nickname,
                'password': registerForm.value.password
            }),
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors'
        }).then(resp => resp.json());
        if (json.status == 200) {
            success('注册成功！请登录');
            clear(registerForm.value);
            registerFormVisible.value = false;
        } else {
            error(json.msg);
        }
    }
}

let loginFormVisible = ref(false);
let loginForm = ref({
    name: '',
    password: ''
});

let login = async () => {
    console.log("点击登录")
    if (loginForm.value.name.length < 4) {
        error('用户名太短');
    } else {
        let json = await fetch(restServerSocket + "/user/login", {
            method: "POST",
            body: JSON.stringify({
                'name': loginForm.value.name,
                'password': loginForm.value.password
            }),
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors'
        }).then(resp => resp.json());
        if (json.status == 200) {
            success('登录成功！');
            loginFormVisible.value = false;
            user.value = json.data;
            user.value.avatar = json.data.avatar;
            clear(loginForm.value)
            isLoggedIn.value = true;
            // emit('refreshLoginState')
        } else {
            error(json.msg);
        }
    }
}

let toUserIndex = () => {
    router.push('/user_index/' + user.value.name);
}

let logout = async () => {
    console.log('退出登录');
    let json = await fetch(restServerSocket + "/user/logout", {
        method: "POST",
        body: JSON.stringify({
            'name': user.value.name
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        mode: 'cors'
    }).then(resp => resp.json());
    if (json.status == 200) {
        success("已退出登录");
        isLoggedIn.value = false;
        // emit('refreshLoginState')
    }
}

function clear(obj) {
    for (let field in obj) {
        console.log('clear: ' + field)
        // obj[field] = '';
    }
}

const handleSelect = (key, keyPath) => {
    console.log(key, keyPath)
}
</script>
  
<style scoped>
.demo-type {
    display: flex;
    flex: 1;
    text-align: center;
}

.flex-grow {
    flex-grow: 1;
}
</style>
  