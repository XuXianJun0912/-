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
        <el-form-item label="广告标题" prop="title">
          <el-input
            v-model="addInfo.title"
            auto-complete="off"
            width="10px"
            class="secPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="广告内容" prop="content">
          <el-input
            v-model="addInfo.content"
            auto-complete="off"
            width="10px"
            class="firPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="奖励积分" prop="points">
          <el-input
            v-model="addInfo.points"
            auto-complete="off"
            width="10px"
            class="secPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="图片地址" prop="imgUrl">
          <el-upload
            ref="uploads"
            :action="upLoadUrl"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :file-list="fileList"
            :before-upload="beforeUpload"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt="" />
          </el-dialog>

          <!-- <el-input
                        v-model="addInfo.imgUrl"
                        auto-complete="off"
                        width="10px"
                        class="secPass"
                        autocomplete="off"
                    >
                    </el-input> -->
        </el-form-item>

        <el-form-item label="广告排序" prop="sortNo">
          <el-input
            v-model="addInfo.sortNo"
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
  name: "CustomerAdd",
  data() {
    return {
      dialogadd: false,
      dialogImageUrl: "",
      upLoadUrl: this.$store.state.url + "/common/upload",
      dialogVisible: false,
      fileList: [],
      addInfo: {
        title: "",
        content: "",
        points: "",
        imgUrl: "",
        sortNo: "",
      },
      rules: {
        title: [
          {
            required: true,
            trigger: "blur",
            message: "请输入广告标题",
          },
        ],
        content: [
          {
            required: true,
            trigger: "blur",
            message: "请输入广告内容",
          },
        ],

        points: [
          {
            required: true,
            message: "请输入奖励积分",
            trigger: "blur",
          },
        ],

        imgUrl: [
          {
            required: false,
            trigger: "blur",
            message: "请输入图片地址",
          },
        ],

        sortNo: [
          {
            required: true,
            trigger: "blur",

            message: "请输入广告排序",
          },
        ],
      },
    };
  },

  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    openDialog(flag, val) {
      this.dialogadd = flag;
      this.$nextTick(() => {
        this.$refs.addInfo.resetFields();
      });
    },

    beforeUpload(file) {
      var testmsg = file.name.substring(file.name.lastIndexOf("." + 1));
      var testmsg = file.type === "image/jpeg";
      var imgSize = file.size / 1024 / 1024;
      console.log(testmsg);
      if (!testmsg) {
        this.$message({
          message: "只能上传jpg、png格式的图片",
          type: "error",
        });
        // 延时器
        setTimeout(() => this.$refs.uploads.clearFiles(), 1000);
      } else if (imgSize > 10) {
        this.$message({
          message: "上传文件不能超过10M",
          type: "error",
        });
        setTimeout(() => this.$refs.uploads.clearFiles(), 1000);
      } else {
        let fd = new FormData();
        fd.append("file", file);
        console.log(fd);
        this.$axios({
          method: "post",
          url: this.upLoadUrl,
          data: fd,
        }).then((res) => {
          console.log(res);
          if (res.data.code == 200) {
            this.dialogImageUrl = res.data.fileName;
            this.$message({
              showClose: true,
              message: res.data.msg,
              type: "success",
            });
          }
        });
      }
    },

    addData(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.dialogImgUrl);
          let params = {
            title: this.addInfo.title,
            content: this.addInfo.content,
            points: this.addInfo.points,
            imgUrl: this.dialogImageUrl,
            sortNo: this.addInfo.sortNo,
          };
          this.$axios
            .post(
              this.$store.state.url + "/backend/advertisement/create",
              params
            )
            .then((res) => {
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

    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleRemove(file, fileList) {},
  },
};
</script>

<style scoped>
.demo-ruleForm {
  position: relative;
  left: -70px;
}
</style>