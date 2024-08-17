// import './assets/main.css'

import { createApp, ref } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'element-plus/dist/index.css'

import config from './config.json';

// const config = await import('./config.json');

const app = createApp(App)
        .use(router)
        .use(ElementPlus, {locale: zhCn,})
        .provide('restServerSocket', config.restServerSocket)
        .provide('staticServerSocket', config.staticServerSocket)
        .provide('isLoggedIn', ref(false))
        .provide('user', ref({
                uid: '',
                name: '',
                avatar: '',
                authority: '',
                signature: ''
        }));

app.mount('#app')
