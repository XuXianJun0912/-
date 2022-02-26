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
        <el-form-item label="广告标题" prop="title">
          <el-input
            v-model="editInfo.title"
            auto-complete="off"
            width="10px"
            class="secPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="广告内容" prop="content">
          <el-input
            v-model="editInfo.content"
            auto-complete="off"
            width="10px"
            class="firPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="奖励积分" prop="points">
          <el-input
            v-model="editInfo.points"
            auto-complete="off"
            width="10px"
            class="secPass"
            autocomplete="off"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="原地址">
          <el-image style="width: 100px; height: 100px" :src="beforeImgUrl">
          </el-image>
        </el-form-item>

        <el-form-item label="图片地址" prop="phone">
          <el-upload
            ref="uploads"
            :action="upLoadUrl"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :before-upload="beforeUpload"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>

        <el-form-item label="排序" prop="sortNo">
          <el-input
            v-model="editInfo.sortNo"
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
      upLoadUrl: "",
      beforeImgUrl: "",
      dialogImageUrl: "",
      upLoadUrl: this.$store.state.url + "/common/upload",
      dialogVisible: false,
      dialogadd: false,
      dialogEdit: false,
      fileList: [],
      editInfo: {
        title: "",
        content: "",
        points: "",
        sortNo: "",
        imgUrl: "",
      },

      rules: {
        title: [
          {
            required: true,
            trigger: "blur",
            message: "请输入广告题目",
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
        sortNo: [
          {
            required: true,
            trigger: "blur",
            message: "请输入排序",
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
      this.dialogEdit = flag;
      this.beforeImgUrl = val[0].imgUrl;
      console.log(this.beforeImgUrl);
      this.$nextTick(() => {
        this.$refs.editInfo.resetFields();
        this.editInfo.title = val[0].title;
        this.editInfo.content = val[0].content;
        this.editInfo.points = val[0].points;
        this.editInfo.sortNo = val[0].sortNo;
        this.editInfo.version = val[0].version;
		this.editInfo.id = val[0].id;
        this.editInfo.imgUrl = val[0].imgUrl;
      });
    },
    cancelDialog() {
      this.dialogEdit = false;
    },
    editData(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.dialogImgUrl);
          let params = {
            title: this.editInfo.title,
            content: this.editInfo.content,
            points: this.editInfo.points,
            imgUrl: this.dialogImageUrl,
            sortNo: this.editInfo.sortNo,
            version: this.editInfo.version,
            id: this.editInfo.id,
          };
          console.log(params);
          this.$axios
            .put(
              this.$store.state.url + "/backend/advertisement/update",
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
  },
};
</script>

<style scoped>
.demo-ruleForm {
  position: relative;
  left: -70px;
}
</style>