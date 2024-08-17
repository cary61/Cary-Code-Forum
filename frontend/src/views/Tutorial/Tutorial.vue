<script setup>
    import { ref,computed, inject, watch } from 'vue';
    import { marked } from 'marked';
    import 'github-markdown-css'
    import { ElMessage } from 'element-plus';
    // import BfAside from './BF_Aside.vue';
    // import TreeAside from './TreeAside.vue';
    import Footer from '../../components/Footer.vue'
    import tutorialNav from '../../assets/tutorial-nav.json'

    
    import { useRouter, useRoute } from "vue-router"
    const router = useRouter();
    const route = useRoute();

    const tutorialTree = buildTree(tutorialNav);
    console.log(tutorialTree);
    const defaultProps = {
        children: 'children',
        label: 'label',
    }
    
    function buildTree(heading) {
        let tree = [];
        for (let subHeading of heading) {
            let key = Object.keys(subHeading)[0];
            let value = subHeading[key];
            let node = {};
            node['label'] = key;
            if (value instanceof Array) {
                node['children'] = buildTree(value);
            } else {
                node['index'] = value;
            }
            tree.push(node);
        }
        
        return tree;
    }
    function toTutorialPage(node) {
        if (node.index != null) {
            router.push('/tutorial/' + node.index);
            // refresh(node.index);
            // console.log(node.index)
            // console.log(file_path())
        }
    }

    // @Deprecated
    // const file_path = () => {
    //     let str = route.params.file_path;
    //     if (str == null || str == '') {
    //         return "index.md";
    //     }
    //     return str.join('/')
    // };

    // const markDownDocument = ref("");
    // const markDownHtml = computed( () => {
    //     return marked.parse(markDownDocument.value);
    // });
    const restServerSocket = inject("restServerSocket");
    const staticServerSocket = inject("staticServerSocket");

    let vhtml = ref('');

    
    watch(() => route.params.file_path, async () => {
            await refresh();
        }, 
        { immediate: true }
    );

    async function refresh() {
        let str = route.params.file_path, file_path;
        if (str == null || str == '') {
            file_path = "index.md";
        } else {
            file_path = str.join('/');
        }
        // console.log('file_path: ' + route.params.file_path)
        let json = await fetch( restServerSocket + "/tutorial_document?file_path=" + file_path)
        .then(resp => resp.json());
        if (json.status == 200) {
            let docHtml = new DOMParser()
                    .parseFromString(marked.parse(json.data), 'text/html')
                    .querySelector('body');
            let endIdx = file_path.lastIndexOf('/');
            let pathPrefix = file_path.substring(0, endIdx);
            swapImgSrc(docHtml, pathPrefix);
            // console.log(docHtml)
            document.getElementById("markDownBlock").innerHTML = docHtml.innerHTML;
            // vhtml.value = docHtml.innerHTML;
        } else {
            ElMessage({
                showClose: true,
                message: '获取教程文章失败',
                grouping: true,
                type: 'warning',
            });
        }
    }

    // function getFileName(file_path) {
    //     return file_path.replace(/(.*\/)*([^.]+).*/ig,"$2");
    // }
    const swapImgSrc = (htmlNode, pathPrefix) => {
        pathPrefix = '/docs/' + pathPrefix;
        for (let imgNode of htmlNode.getElementsByTagName('img')) {
            let oldSrc = imgNode.getAttribute('src');
            if (oldSrc.startsWith('http')) {
                continue;
            }
            if (oldSrc[0] == '.') {
                oldSrc = oldSrc.substring(1);
            }
            if (oldSrc[0] != '/') {
                oldSrc = '/' + oldSrc;
            }
            
            let newSrc = staticServerSocket + pathPrefix + oldSrc;
            imgNode.setAttribute('src', newSrc);
        }
    }

</script>

<template>
    <el-container style="background-color: rgb(244,245,247);">
        <el-aside>
            <!-- <BfAside></BfAside> -->
            <!-- <TreeAside></TreeAside> -->
            <div id="aside-div">
                <el-tree :data="tutorialTree" 
                    :props="defaultProps" 
                    @node-click="toTutorialPage"
                    style="background-color: rgb(244,245,247);">
            </el-tree>
            </div>
        </el-aside>
        <el-container>
            <el-main >
                <div id="markDownBlock"
                    class="markdown-body"
                    v-html="vhtml">
                </div>
            </el-main>
            <el-footer>
                <Footer />
            </el-footer>
        </el-container>
    </el-container>
</template>

<style scoped>
    .markdown-body {
        box-sizing: border-box;
        min-width: 200px;
        max-width: 980px;
        margin: 0 auto;
        padding: 45px;
    }

    @media (max-width: 767px) {
        .markdown-body {
            padding: 15px;
        }
    }

    #aside-div {
        margin-left: 2em;
        overflow-y: auto;
        overflow-x:visible;
        max-height: 800px;
        position: fixed;
        width: 20%;
        height: 80%;
    }

    .el-tree>.el-tree-node>.el-tree-node__content .el-tree-node__label {
        display: block;
        font-size: 1em;
    }
    .el-tree-node__content {
        display: block;
        height: 50px;
    }
</style>