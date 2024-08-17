<script setup>
    import {
        Document,
        Menu as IconMenu,
        Location,
        Setting,
    } from '@element-plus/icons-vue';

    import tutorialNav from '../../assets/tutorial-nav.json';
    
    function title(heading) {
        return Object.keys(heading)[0];
    }
    // for (let heading1 of tutorial_nav) {
    //     console.log(title(heading1))
    //     console.log(heading1[title(heading1)]);
    // }
</script>

<template>
    <el-menu class="el-menu-vertical-demo">
        <div v-for="heading1 in tutorialNav">
            <el-sub-menu v-if="heading1[title(heading1)] instanceof Array" :index="title(heading1)">
                <template #title>{{ title(heading1) }}</template>

                <div v-for="heading2 in heading1[title(heading1)]">
                    <el-sub-menu v-if="heading2[title(heading2)] instanceof Array" :index="title(heading2)">
                        <template #title>{{ title(heading2) }}</template>

                        <div v-for="heading3 in heading2[title(heading2)]">
                            <el-sub-menu v-if="heading3[title(heading3)] instanceof Array" :index="title(heading3)">
                                <template #title>{{ title(heading3) }}</template>

                                <div v-for="heading4 in heading3[title(heading3)]">
                                    <el-sub-menu v-if="heading4[title(heading4)] instanceof Array" :index="title(heading4)">
                                        <template #title>{{ title(heading4) }}</template>

                                        
                                        
                                    </el-sub-menu>
                                    <el-menu-item v-else :index="title(heading4)">{{ title(heading4) }}</el-menu-item>
                                </div>
                                
                            </el-sub-menu>
                            <el-menu-item v-else :index="title(heading3)">{{ title(heading3) }}</el-menu-item>
                        </div>
                        
                    </el-sub-menu>
                    <el-menu-item v-else :index="title(heading2)">{{ title(heading2) }}</el-menu-item>
                </div>

            </el-sub-menu>
            <el-menu-item v-else :index="title(heading1)">{{ title(heading1) }}</el-menu-item>
        </div>

        <el-sub-menu index="1">
            <template #title>
                <el-icon><location /></el-icon>
                <span>Navigator One</span>
            </template>
                <el-menu-item-group title="Group One">
                    <el-menu-item index="1-1">item one</el-menu-item>
                    <el-menu-item index="1-2">item two</el-menu-item>
                </el-menu-item-group>
                <el-menu-item-group title="Group Two">
                    <el-menu-item index="1-3">item three</el-menu-item>
                </el-menu-item-group>
                <el-sub-menu index="1-4">
                    <template #title>item four</template>
                    <el-menu-item index="1-4-1">item one</el-menu-item>
                </el-sub-menu>
        </el-sub-menu>

        <el-menu-item index="2">
            <el-icon><icon-menu /></el-icon>
            <span>Navigator Two</span>
        </el-menu-item>

        <el-menu-item index="3" disabled>
            <el-icon><document /></el-icon>
            <span>Navigator Three</span>
        </el-menu-item>

        <el-menu-item index="4">
            <el-icon><setting /></el-icon>
            <span>Navigator Four</span>
        </el-menu-item>
    </el-menu>
</template>