import Home from '../views/Home.vue';
import Tutorial from '../views/Tutorial/Tutorial.vue'
import Forum from '../views/Forum/Forum.vue'
import Article from '../views/Forum/Article.vue'
import ArticleEdit from '../views/Forum/ArticleEdit.vue'
import About from '../views/About.vue';
import UserIndex from '../views/UserIndex/UserIndex.vue';
import UserManage from '../views/Manage/UserManage.vue';
import ArticleManage from '../views/Manage/ArticleManage.vue';
import CommentManage from '../views/Manage/CommentManage.vue';
import ProblemManage from '../views/Manage/ProblemManage.vue';
import ProblemList from '../views/ProblemList/ProblemList.vue';
import Problem from '../views/Problem/Problem.vue';
import Result from '../views/Result/Result.vue';
import Visualization from '../views/Visualization/Visualization.vue';

export default [
    {
        path: '/',
        component: Home
    },
    {
        path: '/home',
        component: Home
    },
    {
        path: '/tutorial', 
        redirect: to => {
            // 方法接收目标路由作为参数
            // return 重定向的字符串路径/路径对象
            return { path: '/search', query: { q: to.params.searchText } }
        }
    },
    {
        path: '/tutorial/:file_path*', 
        component: Tutorial
    },
    {
        path: '/forum',
        component: Forum
    },
    {
        path: '/forum/article/:article_id',
        component: Article
    },
    {
        path: '/forum/article_edit',
        component: ArticleEdit
    },
    {
        path: '/forum/article_edit/:article_id',
        component: ArticleEdit
    },
    {
        path: '/about',
        component: About
    },
    {
        path: '/user_index/:user_name',
        component: UserIndex
    },
    {
        path: '/manage/user',
        component: UserManage
    },
    {
        path: '/manage/article',
        component: ArticleManage
    },
    {
        path: '/manage/comment',
        component: CommentManage
    },
    {
        path: '/manage/problem',
        component: ProblemManage
    },
    {
        path: '/problem_list',
        component: ProblemList
    },
    {
        path: '/problem_list/:problem_id',
        component: Problem
    },
    {
        path: '/problem_list/result/:polling_id',
        component: Result
    },
    {
        path: '/visualization',
        component: Visualization
    }
];