<template>
    <div>
        <el-dialog
            :visible.sync="dialogadd"
            width="400px"
            :close-on-click-modal = "false"
            @click="resetForm('addInfo')"
        >
            <el-form
                :model="addInfo"
                status-icon
                :rules="rules"
                ref="addInfo"
                label-width="200px"
                class="demo-ruleForm"
            >
                <el-form-item label="用户名" prop="userName">
                    <el-input
                        v-model="addInfo.userName"
                        auto-complete="off"
                        width="10px"
                        class="secPass"
                        autocomplete="off"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="nickName">
                    <el-input
                        v-model="addInfo.nickName"
                        auto-complete="off"
                        width="10px"
                        class="firPass"
                        autocomplete="off"
                    >
                    </el-input>
                </el-form-item>
                
                <el-form-item label="邮箱" prop="email">
                    <el-input
                        v-model="addInfo.email"
                        auto-complete="off"
                        width="10px"
                        class="secPass"
                        autocomplete="off"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item label="电话号码" prop="phonenumber">
                    <el-input
                        v-model="addInfo.phonenumber"
                        auto-complete="off"
                        width="10px"
                        class="secPass"
                        autocomplete="off"
                    >
                    </el-input>
                </el-form-item>

                <el-form-item label="性别" prop="sex">
                    <el-select v-model="addInfo.sex" placeholder="请选择" class="selected">
                        <el-option label="男" value="0"></el-option>
                        <el-option label="女" value="1"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button
                    @click="
                        dialogadd = false;
                        resetForm('addInfo');
                    "
                >
                    取消
                </el-button>
                <el-button type="primary" @click="addData('addInfo')">保存</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name:"UserAdd",
    data(){
        
        return{
            dialogadd:false,
            addInfo:{
                userName:"",
                nickName:"",
                email:"",
                sex:"",
                phonenumber:"",
            },
            rules:{
                userName:[
                    {
                        required:true,
                        trigger:"blur",
                        pattern:/^([\w\@.]{1,14})$/g,
                        message:"请输入正确的用户名",
                    },
                ],
                name:[
                    {
                        required:true,
                        trigger:"blur",
                        pattern:/^([\u4e00-\u9fa5]){2,7}$/,
                        message:"请输入正确的名字",
                    },
                ],
                
                email:[
                    {
                        required:true,
                        trigger:"blur",
                        pattern:/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
                        message:"请输入正确的邮箱",
                    },
                ],
                phonenumber:[
                    {
                        required:true,
                        trigger:"blur",
                        pattern:/^1[3456789]\d{9}$/,
                        message:"请输入正确的电话号码",
                    },
                ],
                sex: [
                    {
                        required : true,
                        pattern: /^(0|1|2)$/,
                        message: "请输入性别",
                        trigger: "blur",
                    },
                ],
                
            },
        };
    },

    methods:{
        resetForm(formName){
            this.$refs[formName].resetFields();
        },

        openDialog(flag,val){
            this.dialogadd = flag;
            this.$nextTick(() => {
                this.$refs.addInfo.resetFields();
            });
        },

        addData(formName){
            this.$refs[formName].validate((valid) => {
                if(valid){
                    let params = {
                        userName: this.addInfo.userName,
                        nickName:this.addInfo.nickName,
                        phonenumber:this.addInfo.phonenumber,
                        email:this.addInfo.email,
                        sex:this.addInfo.sex,
                    };
                    this.$axios.post("/backend/user/create",params).then(res => {
                        if(200 == res.data.code){
                            this.$message({
                                showClose:true,
                                message:res.data.msg,
                                type:"success",
                            });
                            this.$parent.getData();

                            this.dialogadd = false;
                        }else{
                            this.$message({
                                showClose:true,
                                message:res.data.msg,
                                type:"error",
                            });
                        }
                    });
                }else{
                    console.log("error submit!!");
                    return false
                }
            });
        },
    },
}
</script>

<style scoped>
.demo-ruleForm{
    position: relative;
    left: -70px;
}
</style>