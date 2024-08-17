<template>
    <div id="whole">
        <el-row type="flex" justify="center" align="middle">
            <el-col :span="12">
                <div class="mt-4">
                    <el-input v-model="keyword" :placeholder="'请输入' + selectBy" class="input-with-select">
                        <template #prepend>
                            <el-button :icon="Search" />
                        </template>
                        <template #append>
                            <el-button @click="search">
                                搜索
                            </el-button>
                        </template>
                    </el-input>
                </div>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="1">
                <el-text size="large">根据：</el-text>
            </el-col>
            <el-col :span="6">
                <el-radio-group v-model="selectBy">
                    <el-radio-button label="题目id" />
                    <el-radio-button label="题目标题" />
                </el-radio-group>
            </el-col>
            <el-col :span="2">
                <el-button :icon="Plus" circle size="large" type="primary" @click="addButtonClick">
                </el-button>
            </el-col>
        </el-row>

        <el-divider></el-divider>

        <div id="result">
            <div v-for="result of results" :key="result.uid">

                <el-row style="margin-bottom: 1em;" type="flex" justify="center" align="middle">
                    <el-col :span="15">
                        <ProblemTab :id="result.id" :name="result.name" :difficulty="result.difficulty">
                        </ProblemTab>

                    </el-col>
                    <el-col :span="2"></el-col>
                    <el-col :span="3">
                        <el-button type="primary" @click="addCaseButtonClick(result.id, $event)">
                            添加样例
                        </el-button>
                    </el-col>
                    <el-col :span="2">
                        <el-popover :visible="deletePannelVisible" placement="top" :width="160">
                            <p>真的要删除吗？此操作不可逆</p>
                            <div style="text-align: right; margin: 0">
                                <el-button size="small" text @click="deletePannelVisible = false">取消</el-button>
                                <el-button size="small" type="danger"
                                    @click="deletePannelVisible = false; deleteItem(result)">确定</el-button>
                            </div>
                            <template #reference>
                                <el-button @click="deletePannelVisible = true" type="danger">删除</el-button>
                            </template>
                        </el-popover>
                    </el-col>
                    <el-divider></el-divider>
                </el-row>
            </div>
        </div>

        <!-- 添加题目 -->
        <el-dialog v-model="addPanelVisivle" title="添加题目" width="60%">
            <el-form :model="newProblem">
                <el-form-item label="难度" :label-width="formLabelWidth">
                    <el-select v-model="newProblem.difficulty" class="m-2" placeholder="Select">
                        <el-option v-for="item in difficultyOptions" :key="item.value" :label="item.label"
                            :value="item.value" />
                    </el-select>
                </el-form-item>
                <el-form-item label="标题" :label-width="formLabelWidth">
                    <el-input v-model="newProblem.name" placeholder="请输入标题" />
                </el-form-item>
                <el-form-item label="背景" :label-width="formLabelWidth">
                    <el-input v-model="newProblem.background" type="textarea" placeholder="输入背景（非必须）" />
                </el-form-item>
                <el-form-item label="描述" :label-width="formLabelWidth">
                    <el-input v-model="newProblem.description" type="textarea" placeholder="输入题目描述" />
                </el-form-item>
                <el-form-item label="输入格式" :label-width="formLabelWidth">
                    <el-input v-model="newProblem.input_format" type="textarea" placeholder="题目的数据输入格式" />
                </el-form-item>
                <el-form-item label="输出格式" :label-width="formLabelWidth">
                    <el-input v-model="newProblem.output_format" type="textarea" placeholder="题目的数据输出格式" />
                </el-form-item>
                <el-form-item label="提示/说明" :label-width="formLabelWidth">
                    <el-input v-model="newProblem.tip" type="textarea" placeholder="给出数据范围和一定的提示" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="addPanelVisivle = false">取消</el-button>
                    <el-button type="primary" @click="addProblem">提交</el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 添加样例 -->
        <el-dialog v-model="addCasePannelVisible" title="添加样例" width="60%">
            <div v-for="cas of cases" style="position: relative;">
                <el-form :model="cas">
                    <el-text>{{ '样例' + cas.serial_id }}</el-text>
                    <el-form-item label="样例输入" :label-width="formLabelWidth">
                        <el-input v-model="cas.input" type="textarea" placeholder="样例的控制台输入"
                            :autosize="{ minRows: 3, maxRows: 20 }" />
                    </el-form-item>
                    <el-form-item label="样例输出" :label-width="formLabelWidth">
                        <el-input v-model="cas.output" type="textarea" placeholder="样例的答案"
                            :autosize="{ minRows: 3, maxRows: 20 }" />
                    </el-form-item>
                    <el-form-item label="时间要求 (ms)" :label-width="formLabelWidth">
                        <el-input v-model="cas.max_time" placeholder="以毫秒计的样例最长时间" />
                    </el-form-item>
                    <el-form-item label="内存要求 (MB)" :label-width="formLabelWidth">
                        <el-input v-model="cas.max_memory" placeholder="以Mb计的样例最大内存" />
                    </el-form-item>
                </el-form>
                <p></p>
                <el-button :icon="Delete" circle @click="removeCase(cas.serial_id)"
                    style="position: absolute; left: 0.2em; top: 2em;">
                </el-button>
                <el-divider></el-divider>
            </div>
            <el-row type="flex" justify="space-around" align="middle">
                <el-col :span="1">
                    <el-button :icon="Plus" circle size="large" @click="addNewCase"></el-button>
                </el-col>
            </el-row>
            <el-divider></el-divider>

            <div style="position: relative">
                <el-text>从模板生成</el-text>
                <el-form :model="newVariable">
                    <el-form-item label="类型" :label-width="formLabelWidth">
                        <el-select v-model="newVariable.type" class="m-2" placeholder="Select">
                            <el-option v-for="item in variableOptions" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="变量名" :label-width="formLabelWidth">
                        <el-input v-model="newVariable.name" placeholder="请输入变量名" />
                    </el-form-item>
                    <el-form-item label="最小值" :label-width="formLabelWidth"
                        v-if="newVariable.type == 'number' || newVariable.type == 'number_array'">
                        <el-input v-model="newVariable.min" placeholder="" />
                    </el-form-item>
                    <el-form-item label="最大值" :label-width="formLabelWidth"
                        v-if="newVariable.type == 'number' || newVariable.type == 'number_array'">
                        <el-input v-model="newVariable.max" placeholder="" />
                    </el-form-item>
                    <el-form-item label="长度" :label-width="formLabelWidth"
                        v-if="newVariable.type == 'string' || newVariable.type == 'number_array'">
                        <el-input v-model="newVariable.total_length" placeholder="" />
                    </el-form-item>
                    <el-form-item label="重复性" :label-width="formLabelWidth"
                        v-if="newVariable.type == 'string' || newVariable.type == 'number_array'">
                        <el-select v-model="newVariable.duplicate_able" class="m-2" placeholder="Select">
                            <el-option v-for="item in duplicate_able_options" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="字符格式" :label-width="formLabelWidth" v-if="newVariable.type == 'string'">
                        <el-select v-model="newVariable.letter_case" class="m-2" placeholder="Select">
                            <el-option v-for="item in letterCaseOptions" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-button @click="addNewVariable" style="position: absolute; right: 1em; top: 1em">
                        添加变量
                    </el-button>
                    <p></p>
                </el-form>
            </div>
            <div v-if="variables.length != 0" style="margin: auto; width: 96%">
                <el-row>
                    <el-col :span="3">变量名</el-col>
                    <el-col :span="4">类型</el-col>
                    <el-col :span="4">最小值</el-col>
                    <el-col :span="4">最大值 </el-col>
                    <el-col :span="3">长度</el-col>
                    <el-col :span="3">重复性</el-col>
                    <el-col :span="3">字符格式</el-col>
                </el-row>
                <hr>
            </div>
            <div v-for="variable of variables" style="margin: auto; width: 96%">
                <el-row>
                    <el-col :span="3">
                        {{ variable.name }}
                    </el-col>
                    <el-col :span="4">
                        {{ variableOptionsResolve(variable.type) }}
                    </el-col>
                    <el-col :span="4">
                        <span v-if="variable.type == 'number' || variable.type == 'number_array'">
                            {{ variable.min }}
                        </span>
                        <span v-else style="color: gray">
                            N/A
                        </span>
                    </el-col>
                    <el-col :span="4">
                        <span v-if="variable.type == 'number' || variable.type == 'number_array'">
                            {{ variable.max }}
                        </span>
                        <span v-else style="color: gray">
                            N/A
                        </span>
                    </el-col>
                    <el-col :span="3">
                        <span v-if="variable.type == 'string' || variable.type == 'number_array'">
                            {{ variable.total_length }}
                        </span>
                        <span v-else style="color: gray">
                            N/A
                        </span>
                    </el-col>
                    <el-col :span="3">
                        <span v-if="variable.type == 'string' || variable.type == 'number_array'">
                            {{ duplicate_able_options_resolve(variable.duplicate_able) }}
                        </span>
                        <span v-else style="color: gray">
                            N/A
                        </span>
                    </el-col>
                    <el-col :span="3">
                        <span v-if="variable.type == 'string'">
                            {{ letterCaseOptionsResolve(variable.letter_case) }}
                        </span>
                        <span v-else style="color: gray">
                            N/A
                        </span>
                    </el-col>
                </el-row>
                <hr>
            </div>
            <div>
                <el-text>模板字符串</el-text>
                <el-input type="textarea" v-model="templateString" :autosize="{ minRows: 3, maxRows: 8 }">
                </el-input>
                <p></p>
                <div style="margin: auto; width: 20%">
                    <el-button type="primary" @click="generateAndAdd">
                        生成并添加到样例
                    </el-button>
                </div>
            </div>
            
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="addCasePannelVisible = false">取消</el-button>
                    <el-button type="primary" @click="addCase">提交</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>
<script setup>
import { reactive, ref, inject } from 'vue';
import axios from 'axios';
import { Search } from '@element-plus/icons-vue';
import { success, warning, error } from '../../utils/message.js';
import ProblemTab from '../ProblemList/ProblemTab.vue';
import { Plus, Delete } from '@element-plus/icons-vue';

const restServerSocket = inject("restServerSocket");
const staticServerSocket = inject("staticServerSocket");

const selectBy = ref('题目id');
const keyword = ref('');

const results = reactive([]);
const deletePannelVisible = ref(false);
const addPanelVisivle = ref(false);

const newProblem = reactive({
    name: '',
    background: '',
    description: '',
    input_format: '',
    output_format: '',
    tip: '',
    difficulty: 'EASY'
});

const formLabelWidth = ref("10em");

const addProblem = () => {
    axios.post(restServerSocket + '/judge/problem', newProblem)
        .then(resp => resp.data)
        .then(data => {
            if (data.status == 200) {
                success("添加成功");
            } else {
                error(data.msg);
            }
            addPanelVisivle.value = false;
        })
}

const difficultyOptions = [
    {
        value: 'EASY',
        label: 'EASY'
    },
    {
        value: 'MIDDLE',
        label: 'MIDDLE'
    },
    {
        value: 'HARD',
        label: 'HARD'
    }
];

const addCasePannelVisible = ref(false);

const addCaseButtonClick = (id, e) => {
    let target = e.target;
    if (target.nodeName == "SPAN") {
        target = e.target.parentNode;
    }
    target.blur();
    problemId.value = id;
    addCasePannelVisible.value = true;
}

const cases = reactive([
    {
        serial_id: 1,
        input: '',
        output: '',
        max_time: 2000,
        max_memory: 100
    }
]);
const serial = ref(2);

const addNewCase = (e) => {
    let target = e.target;
    if (target.nodeName == "SPAN") {
        target = e.target.parentNode;
    }
    target.blur();
    let id = serial.value;
    serial.value++;
    cases.push({
        serial_id: id,
        input: '',
        output: '',
        max_time: 2000,
        max_memory: 100
    });
}

const problemId = ref(0);

const addCase = () => {
    cases.forEach(e => e.max_memory *= 1024 * 1024);
    axios.post(restServerSocket + '/judge/case', {
        problem_id: problemId.value,
        cases
    }).then(resp => resp.data)
        .then(data => {
            cases.length = 0;
            if (data.status == 200) {
                success("添加成功");
            } else {
                error(data.msg);
            }
            addCasePannelVisible.value = false;
            serial.value = 1;
        })
}

const removeCase = (id) => {
    for (let i = 0; i < cases.length; i++) {
        if (cases[i].serial_id == id) {
            cases.splice(i, 1);
            return;
        }
    }
}

const search = async () => {
    results.length = 0;
    if (selectBy.value == '题目id') {
        await searchById();
    } else if (selectBy.value == '题目标题') {
        await searchByName();
    }
    if (results.length == 0) {
        warning("未搜索到匹配的结果");
    } else {
        success('搜索完成')
    }
}

const addButtonClick = (e) => {
    let target = e.target;
    if (target.nodeName == "SPAN") {
        target = e.target.parentNode;
    }
    target.blur();
    addPanelVisivle.value = true;
}


const searchById = async () => {
    await axios.get(restServerSocket + '/judge/problem', {
        params: { 'problem_id': keyword.value }
    }).then(resp => resp.data)
        .then(data => {
            if (data.data != null) {
                results.push(data.data)
            }
        });
}


const searchByName = async () => {
    await axios.get(restServerSocket + '/judge/problem', {
        params: { 'problem_name': keyword.value }
    }).then(resp => resp.data)
        .then(data => {
            if (data.data != null) {
                results.push(data.data)
            }
        });
}

const deleteItem = async (item) => {
    await axios.delete(restServerSocket + '/manage/comment', {
        params: { id: item.id }
    }).then(resp => resp.data)
        .then(data => {
            if (data.status == 200) {
                success('删除成功')
                for (let i = 0; i < results.length; i++) {
                    if (results[i].uid == item.uid) {
                        results.splice(i, 1);
                        break;
                    }
                }
            } else {
                error('删除失败')
            }
        })
}

const variableOptions = [
    {
        label: '数字',
        value: 'number'
    },
    {
        label: '数列',
        value: 'number_array'
    },
    {
        label: '字符串',
        value: 'string'
    }
];

const variableOptionsResolve = (value) => {
    for (let variableOption of variableOptions) {
        if (value == variableOption.value) {
            return variableOption.label;
        }
    }
}

const duplicate_able_options = [
    {
        label: '可重复',
        value: true
    },
    {
        label: '不可重复',
        value: false
    },
];

const duplicate_able_options_resolve = (value) => {
    for (let duplicate_able_option of duplicate_able_options) {
        if (value == duplicate_able_option.value) {
            return duplicate_able_option.label;
        }
    }
}

const letterCaseOptions = [
    {
        label: '大写',
        value: 'uppercase'
    },
    {
        label: '小写',
        value: 'lowercase'
    },
    {
        label: '均可',
        value: 'whatever'
    }
];

const letterCaseOptionsResolve = (value) => {
    for (let letterCaseOption of letterCaseOptions) {
        if (value == letterCaseOption.value) {
            return letterCaseOption.label;
        }
    }
}

const newVariable = reactive({
    type: 'number',
    name: '',
    min: 0,
    max: 100,

    total_length: 10,

    duplicate_able: true,

    letter_case: 'whatever',

});

const variables = reactive([]);
const templateString = ref('');

const addNewVariable = (e) => {
    let target = e.target;
    if (target.nodeName == "SPAN") {
        target = e.target.parentNode;
    }
    target.blur();
    if (newVariable.name == '') {
        warning('变量名不能为空');
        return;
    }
    for (let variable of variables) {
        if (newVariable.name == variable.name) {
            warning('变量名重复');
            return;
        }
    }
    variables.push({
        type: newVariable.type,
        name: newVariable.name,
        min: newVariable.min,
        max: newVariable.max,
        total_length: newVariable.total_length,
        duplicate_able: newVariable.duplicate_able,
        letter_case: newVariable.letter_case
    });
    newVariable.name = '';
}

const generateNumber = (min, max) => {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

const generateNumberArray = (min, max, total_length, duplicate_able) => {
    let arr = [];
    for (let i = 0; i < total_length; i++) {
        let num;
        do {
            num = generateNumber(min, max);
        } while (!duplicate_able && arr.findIndex((e) => e == num) != -1)
        arr.push(num);
    }
    return arr.join(' ');
}

const generateString = (total_length, duplicate_able, letter_case) => {
    let arr = [];
    for (let i = 0; i < total_length; i++) {
        let ch;
        do {
            ch = generateCharacter(letter_case);
        } while (!duplicate_able && arr.findIndex((e) => e == ch) != -1)
        arr.push(ch);
    }
    return arr.join('');
}

const generateCharacter = (letter_case) => {
    let index = generateNumber(0, 25);
    if (letter_case == 'whatever') {
        let a = generateNumber(0, 100);
        if (a % 2 == 0) {
            letter_case = 'uppercase';
        } else {
            letter_case = 'lowercase';
        }
    }
    if (letter_case == 'uppercase') {
        return String.fromCharCode(65 + index);
    } else {
        return String.fromCharCode(97 + index);
    }
}

const generateVariable = (variable) => {
    if (variable.type == 'number') {
        return generateNumber(variable.min, variable.max);
    }
    if (variable.type = 'number_array') {
        return generateNumberArray(variable.min, variable.max, variable.total_length, variable.duplicate_able);
    }
    if (variable.type == 'string') {
        return generateString(variable.total_length, variable.duplicate_able, variable.letter_case);
    }
}

const generate = () => {
    let declareString = '';
    for (let variable of variables) {
        if (variable.type == 'number') {
            declareString += `let ${variable.name} = generateNumber(${variable.min}, ${variable.max});`;
        } else if (variable.type == 'number_array') {
            declareString += `let ${variable.name} = generateNumberArray(${variable.min}, ${variable.max}, ${variable.total_length}, "${duplicate_able_options_resolve(variable.duplicate_able)}"  );`;
        } else if (variable.type == 'string') {
            declareString += `let ${variable.name} = generateString( ${variable.total_length}, ${variable.duplicate_able}, "${variable.letter_case}" );`;
        }
    }
    let resresABCD___;
    let strstrABCD___ = declareString + " resresABCD___ = eval('\`' + templateString.value + '\`')  ";
    console.log('eval string: \n' + strstrABCD___)
    try {
        eval(strstrABCD___);
    } catch (e) {
        warning("模板内含有未定义的变量");
        throw e;
    }
    console.log('result string: \n' + resresABCD___)
    return resresABCD___;


    // for (let variable of variables) {
    //     eval('var ' + variable.name + " = generateVariable(variable)");
    // }
    // return eval('`' + templateString.value + '`');
}

const generateAndAdd = (e) => {
    let target = e.target;
    if (target.nodeName == "SPAN") {
        target = e.target.parentNode;
    }
    target.blur();
    if (templateString.value == '') {
        warning('模板字符串为空');
        return;
    }
    let result = generate();
    let id = serial.value;
    serial.value++;
    cases.push({
        serial_id: id,
        input: result,
        output: '',
        max_time: 2000,
        max_memory: 100
    });
}

</script>

<style scoped>
#whole {
    width: 80%;
    margin: auto;
    margin-top: 1em;
}
</style>