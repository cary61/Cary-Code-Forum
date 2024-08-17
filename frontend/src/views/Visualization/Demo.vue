<script setup>
import { ref, reactive } from 'vue';
class Grid {
    constructor(rows, cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = [];
        for (let i = 0; i < this.rows; i++) {
            this.board.push(new Array());
            for (let j = 0; j < this.cols; j++) {
                this.board[i].push(new Point());
            }
        }
    }
    ajust() {
        let newBoard = [];
        for (let i = 0; i < this.rows; i++) {
            newBoard.push(new Array());
            for (let j = 0; j < this.cols; j++) {
                newBoard[i].push(null);
            }
        }
        let oldRows = this.board.length;
        let oldCols = this.board[0]?.length;
        for (let i = 0; i < this.rows; i++) {
            for (let j = 0; j < this.cols; j++) {
                if (i < oldRows && j < oldCols) {
                    newBoard[i][j] = this.board[i][j];
                } else {
                    newBoard[i][j] = new Point();
                }
            }
        }
        this.board = newBoard;
    }
}
class Point {
    constructor() {
        this.state = 0;
    }
}
const settings = reactive({
    rows: 1,
    cols: 1,
});

const ajustAble = ref(true);
const fill = ref(1);

const resize = () => {
    grid.rows = settings.rows;
    grid.cols = settings.cols;
    grid.ajust();
}

const grid = reactive(new Grid(settings.rows, settings.cols));

const changeState = (point) => {
    if (point.state == 0) {
        point.state = fill.value;
    } else {
        point.state = 0;
    }
}
const diretions = [
    [-1, 0], [1, 0], [0, -1], [0, 1]
];
const dfs = (x, y) => {
    grid.board[x][y] = 9
    if (grid.board[x][y] != 9) {
        return true;
    }
    for (let dir of diretions) {
        let x1 = x + dir[0], y1 = y + dir[1];
        if (0 <= x1 && x1 < grid.rows &&
                0 <= y1 && y1 < grid.cols) {
            setTimeout(() => dfs(x1, y1), 300);
        }
    }
}
</script>

<template>
    <div id="board">
        111
        <div v-for="line of grid.board">
            <span v-for="point of line" @click="changeState(point)">
                {{ point.state == null ? 3 : point.state }}
            </span>
        </div>
    </div>
    <input v-model="settings.rows">
    <input v-model="settings.cols">
    <input v-model="fill">
    <button @click="resize">
        resize
    </button>
    <button @click="dfs(0,0)">go</button>
    <div>
        <a href="./pages/graph/graph.html">a</a>
    </div>
    <div>
        <a href="pages/sorting/sorting_page.html">b</a>
    </div>
    
</template>

<style scoped>

</style>