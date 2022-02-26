<template>
  <div class="user">
    <!--查询用户 -->
    <el-card class="box-card">
      <el-form
        :model="imform"
        ref="imform"
        label-width="100px"
        :rules="rule1"
        :inline="true"
      >
        <el-form-item label prop="searchValue" class="name">
          <el-input
            v-model="imform.searchValue"
            class="nameBtn"
            placeholder="提交人/意见标题"
          ></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="search()" class="addBtn"
        >查询</el-button
      >
      <el-button @click="resetForm('imform')" class="resetBtn">重置</el-button>
    </el-card>

    <!--客户列表-->
    <el-card>
      <sirui-table
        :data="tableData"
        :page="page"
        :tableHeight="tableHeight"
        :loading="load"
        @handleSelectionChange="handleSelectionChange"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          v-for="item in columnList"
          :key="item.num"
          :label="item.lable"
          :width="item.width"
          :prop="item.prop"
        ></el-table-column>
      </sirui-table>
    </el-card>

    <!-- 新增弹出框 -->
    <!-- <customer-add ref="addData"></customer-add>
        <customer-edit ref="editData"></customer-edit> -->
  </div>
</template>
<script>
import SiruiTable from "../../components/SiruiTable/index";
// import CustomerAdd from "./CustomerAdd" ;
// import CustomerEdit from "./CustomerEdit";
export default {
  components: {
    SiruiTable,
    // CustomerAdd,
    // CustomerEdit,
  },
  name: "ViewManages",
  data() {
    var checkName = (rule, value, callback) => {
      var userName = /^[ a-zA-Z][a-zA-Z0-9]{3,15}$/;
      var email = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
      var phone = /^1[ 3456789]\d{9}$ /;
      var name = /([\*\“\'<>\/])+/;
      setTimeout(() => {
        if (
          !email.test(value) &&
          !userName.test(value) &&
          !phone.test(value) &&
          name.test(value)
        ) {
          callback(new Error("请输入正确的用户名/客户名/手机号/邮箱"));
        } else {
          callback();
        }
      }, 100);
    };

    return {
      tableHeight: 400,
      load: false,
      dialogVisible: false,
      pwdForm: {
        pass: "",
        checkPass: "",
      },
      imform: {
        searchValue: "",
      },
      rule1: {
        name: {
          validator: checkName,
        },
      },

      columnList: [
        {
          prop: "id",
          lable: "编号",
          width: 100,
          num: 1,
        },
        {
          prop: "nickName",
          lable: "提交人",
          width: 180,
          num: 2,
        },
        {
          prop: "title",
          lable: "意见标题",
          width: 180,
          num: 3,
        },
        {
          prop: "content",
          lable: "意见内容",
          width: 200,
          num: 4,
        },
        {
          prop: "createTime",
          lable: "提交时间",
          width: 150,
          num: 5,
        },
        {
          prop: "delFlag",
          lable: "删除状态",
          width: 100,
          num: 6,
        },
      ],
      page: {
        pages: 4,
        total: 0,
        pageSize: 10, //每页显示几条
        pageNum: 1, //第几页
      },

      tableData: [],

      multipleSelection: [],

      ostatus: "",
    };
  },

  mounted() {
    this.getData();
  },

  methods: {
    //转换汉字
    words() {
      for (let i = 0; i < this.tableData.length; i++) {
        if (this.tableData[i].delFlag == 1) {
          this.tableData[i].delFlag = "已删除";
        } else if (this.tableData[i].delFlag == 0) {
          this.tableData[i].delFlag = "未删除";
        }
      }
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.getData();
    },

    resetPwd() {
      //重置密码，调接口
      if (this.multipleSelection.length >= 1) {
        this.$confirm("是否重置密码", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let params = {
              userIds: [],
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.userIds.push(this.multipleSelection[i].userId);
            }
            params.userIds = params.userIds.join(",");
            this.$axios
              .put("prop-api/backend/user/reset" + params.userIds)
              .then((res) => {
                if (res.data.code == 200) {
                  this.words();
                  this.$message({
                    type: "success",
                    message: res.data.msg,
                  });
                } else {
                  this.$message({
                    type: "error",
                    message: res.data.msg,
                  });
                }
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消重置密码",
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作",
        });
      }
    },

    search() {
      this.getData();
    },

    //查询数据列表
    getData() {
      this.$axios
        .get(
          `/backend/advice/list?pageNum=${this.page.pageNum}&pageSize=${this.page.pageSize}&searchValue=${this.imform.searchValue}`
        )
        .then((resp) => {
          console.log(resp);
          if (resp) {
            this.tableData = resp.data.rows;
            this.page.total = resp.data.total;
            this.page.pages = Math.round(resp.data.total / resp.data.pageSize);
            this.words();
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
        });
    },

    //选中触发事件
    handleSelectionChange(row) {
      this.multipleSelection = row;
      console.log(this.multipleSelection);
    },

    //第几页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getData();
    },

    //抛出每页条数事件
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getData();
    },

    addData() {
      this.$refs.addData.openDialog(
        true,
        JSON.parse(JSON.stringify(this.multipleSelection))
      );
    },

    //显示新修改弹出窗
    editData() {
      //debugger;
      if (1 == this.multipleSelection.length) {
        this.$refs.editData.openDialog(
          true,
          JSON.parse(JSON.stringify(this.multipleSelection))
        );
      } else if (this.multipleSelection.length > 1) {
        this.$message({
          type: "info",
          message: "请选择一条数据进行操作",
        });
      } else {
        this.$message({ type: "info", message: "请选择数据进行操作" });
      }
    },

    start() {},
    forbid() {},
    Delete() {},
  },
};
</script>
<style scoped>
.user {
  position: absolute;
  width: 1200px;
  height: 800px;
  top: 70px;
  left: 280px;
}
</style>
