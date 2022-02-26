<template>
  <div>
    <el-dialog
      :visible.sync="dialogEdit"
      width="400px"
      :close-on-click-modal="false"
      @click="resetForm('editInfo')"
    >
      <el-form
        :model="editInfo"
        status-icon
        :rules="rules"
        ref="editInfo"
        label-width="200px"
        class="demo-ruleForm"
      >
        <el-form-item label="客户名" prop="userName">
          <el-input
            v-model="editInfo.userName"
            auto-complete="off"
            width="10px"
            class="secPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="nickName">
          <el-input
            v-model="editInfo.nickName"
            auto-complete="off"
            width="10px"
            class="firPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="性别" prop="sex">
          <el-select
            v-model="editInfo.sex"
            placeholder="请选择"
            class="selected"
          >
            <el-option label="男" value="0"></el-option>
            <el-option label="女" value="1"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="editInfo.email"
            auto-complete="off"
            width="10px"
            class="secPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="电话号" prop="phonenumber">
          <el-input
            v-model="editInfo.phonenumber"
            auto-complete="off"
            width="10px"
            class="secPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          @click="
            dialogEdit = false;
            resetForm('editInfo');
          "
        >
          取消
        </el-button>
        <el-button type="primary" @click="editData('editInfo')">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      dialogEdit: false,
      editInfo: {
        // userName: "",
        nickName: "",
        email: "",
        sex: "",
        phonenumber: "",
        userId: "",
        version: "",
      },
      rules: {
        // userName: [
        //   {
        //     required: true,
        //     trigger: "blur",
        //     pattern: /^([\w\@.]{1,14})$/g,
        //     message: "请输入正确的用户名",
        //   },
        // ],
        nickName: [
          {
            required: true,
            trigger: "blur",
            pattern: /^([\u4e00-\u9fa5]){2,7}$/,
            message: "请输入正确的名字",
          },
        ],
        sex: [
          {
            required: true,
            pattern: /^(0|1|2)$/,
            message: "请输入性别",
            trigger: "blur",
          },
        ],
        email: [
          {
            required: false,
            trigger: "blur",
            pattern: /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
            message: "请输入正确的邮箱",
          },
        ],
        phonenumber: [
          {
            required: true,
            trigger: "blur",
            pattern: /^1[3456789]\d{9}$/,
            message: "请输入正确的电话号码",
          },
        ],
      },
    };
  },
  methods: {
    resetForm(editInfo) {
      this.$refs[editInfo].resetFields();
    },

    openDialog(flag, val) {
      console.log("value" + JSON.stringify(val));
      this.dialogEdit = flag;
      this.$nextTick(() => {
        this.$refs.editInfo.resetFields();
        this.editInfo.email = val[0].email;
        this.editInfo.nickName = val[0].nickName;
        this.editInfo.userName = val[0].userName;
        this.editInfo.phonenumber = val[0].phonenumber;
        this.editInfo.sex = val[0].sex;
        this.editInfo.userId = val[0].userId;
        this.editInfo.version = val[0].version;
      });
    },
    cancelDialog() {
      this.dialogEdit = false;
    },
    editData(editInfo) {
      this.$refs[editInfo].validate((valid) => {
        if (valid) {
          let params = {
            userName: this.editInfo.userName,
            nickName: this.editInfo.nickName,
            phonenumber: this.editInfo.phonenumber,
            email: this.editInfo.email,
            sex: this.editInfo.sex,
            userId: this.editInfo.userId,
            version: this.editInfo.version,
          };
          this.$axios.put("/backend/customer/update", params).then((res) => {
            if (200 == res.data.code) {
              this.$message({
                showClose: true,
                message: res.data.msg,
                type: "success",
              });

              this.$parent.getData();

              this.dialogadd = false;
            } else {
              this.$message({
                showClose: true,
                message: res.data.msg,
                type: "error",
              });
            }
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>

<style scoped>
.demo-ruleForm {
  position: relative;
  left: -70px;
}
</style>