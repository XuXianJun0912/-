<template>
  <div>
    <el-dialog
      :visible.sync="dialogadd"
      width="400px"
      :close-on-click-modal="false"
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
          <!-- <el-input
            v-model="addInfo.boxStandard"
            auto-complete="off"
            width="10px"
            class="secPass"
            autocomplete="off"
          >
          </el-input> -->
          <el-select
            v-model="addInfo.boxStandard"
            placeholder="请选择"
            @change="jifen()"
          >
            <el-option
              v-for="item in option"
              :key="item.id"
              :label="item.boxStandard"
              :value="item.boxStandard"
            ></el-option>
          </el-select>
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
  name: "BoxNumberAdd",
  data() {
    return {
      dialogadd: false,
      addInfo: {
        boxStandard: "",
        boxUnitPrice: "",
        remark: "",
      },
      option: [],
      rules: {
        boxStandard: [
          {
            required: true,
            trigger: "blur",
            message: "请输入箱子规格",
            pattern: /\d+\*\d+\*\d+/,
          },
        ],

        boxUnitPrice: [
          {
            required: true,
            message: "请输入所需积分",
            trigger: "blur",
          },
        ],

        remark: [
          {
            required: true,
            message: "请输入备注信息",
            trigger: "blur",
          },
        ],
      },
    };
  },

  mounted() {
    this.getSelectBox();
  },

  methods: {
    //   积分
    jifen() {
      for (const i in this.option) {
        if (this.option[i].boxStandard == this.addInfo.boxStandard) {
          this.addInfo.boxUnitPrice = this.option[i].boxUnitPrice;
        }
      }
    },

    // 查询箱子种类
    getSelectBox() {
      this.$axios.get("/backend/box/standard/select").then((resp) => {
        console.log(resp);
        if (resp) {
          if (200 == resp.data.code) {
            this.option = resp.data.data;
            this.$message({
              showClose: true,
              message: "查询成功",
              type: "success",
            });
          } else {
            this.$message({
              showClose: true,
              message: "查询失败",
              type: "error",
            });
          }
        }
      });
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    openDialog(flag, val) {
      this.dialogadd = flag;
      this.$nextTick(() => {
        this.$refs.addInfo.resetFields();
      });
    },

    addData(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let params = {
            boxStandard: this.addInfo.boxStandard,
            boxUnitPrice: this.addInfo.boxUnitPrice,
            remark: this.addInfo.remark,
          };
          this.$axios.post("backend/box/info/create", params).then((res) => {
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