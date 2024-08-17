<script setup>
    import { ref, inject } from 'vue';
    import axios from 'axios';
    import Header from './components/Header.vue';
    const restServerSocket = inject("restServerSocket");
    const staticServerSocket = inject("staticServerSocket");
    const isLoggedIn = inject('isLoggedIn');
    const user = inject('user');

    

</script>

<template>
    <div class="common-layout">
      <el-container>
        <el-header>
            <Suspense>
              <Header/>
            </Suspense>
        </el-header>
        <Suspense>
          <router-view ></router-view>
        </Suspense>
        
        
      </el-container>
    </div>
    <el-backtop :right="100" :bottom="100" />
  </template>
  

<style scoped>
</style>
