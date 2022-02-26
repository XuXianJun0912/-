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
                <el-form-item label="箱子规格" prop="boxStandard">
                    <el-input
                        v-model="addInfo.boxStandard"
                        auto-complete="off"
                        width="10px"
                        class="secPass"
                        autocomplete="off"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item label="库存数量" prop="inventoryNumber">
                    <el-input
                        v-model="addInfo.inventoryNumber"
                        auto-complete="off"
                        width="10px"
                        class="firPass"
                        autocomplete="off"
                    >
                    </el-input>
                </el-form-item>

                <el-form-item label="所需积分" prop="boxUnitPrice">
                    <el-input
                        v-model="addInfo.boxUnitPrice"
                        auto-complete="off"
                        width="10px"
                        class="secPass"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>

                <el-form-item label="备注" prop="remark">
                    <el-input
                        v-model="addInfo.remark"
                        auto-complete="off"
                        width="10px"
                        class="secPass"
                        autocomplete="off"
                    ></el-input>
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
    name:"BoxAdd",
    data(){
        
        return{
            dialogadd:false,
            addInfo:{
                boxStandard:"",
                inventoryNumber:"",
                boxUnitPrice:"",
                remark:"",
            },
            rules:{
                boxStandard:[
                    {
                        required:true,
                        trigger:"blur",
                        message:"请输入箱子规格",
                        pattern: /\d+\*\d+\*\d+/,
                    },
                ],
                inventoryNumber:[
                    {
                        required:true,
                        trigger:"blur",
                        message:"请输入库存数量",
                    },
                ],
            
                boxUnitPrice: [
                    {
                        required : true,                        
                        message: "请输入所需积分",
                        trigger: "blur",
                    },
                ],

                remark: [
                    {
                        required : true,                        
                        message: "请输入备注信息",
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
                        boxStandard: this.addInfo.boxStandard,
                        inventoryNumber:this.addInfo.inventoryNumber,
                        boxUnitPrice:this.addInfo.boxUnitPrice,
                        remark:this.addInfo.remark,
                    };
                    this.$axios.post("/backend/box/standard/create",params).then(res => {
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