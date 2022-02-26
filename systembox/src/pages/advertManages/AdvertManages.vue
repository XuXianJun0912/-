<template>
  <div class="user">
    <!--查询广告 -->
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
            placeholder="广告标题/奖励积分/图片地址"
          ></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="search()" class="addBtn"
        >查询</el-button
      >
      <el-button @click="resetForm('imform')" class="resetBtn">重置</el-button>
    </el-card>

    <!--广告列表-->
    <el-card>
      <el-button type="primary" @click="addData()">新增</el-button>
      <el-button type="primary" @click="editData()">编辑</el-button>
      <el-button type="primary" @click="start()">启用</el-button>
      <el-button type="primary" @click="forbid()">停用</el-button>
      <el-button type="primary" @click="Delete()">删除</el-button>

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
          v-for="item in columnList1"
          :key="item.num"
          :label="item.lable"
          :width="item.width"
          :prop="item.prop"
        >
        </el-table-column>

        <el-table-column
          prop="imgUrl"
          label="图片地址"
          width="180"
          align="center"
          header-align="center"
        >
          <template slot-scope="scope">
            <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.imgUrl"
            >
            </el-image>
          </template>
        </el-table-column>

        <el-table-column
          v-for="item in columnList2"
          :key="item.num"
          :label="item.lable"
          :width="item.width"
          :prop="item.prop"
        ></el-table-column>
      </sirui-table>
    </el-card>

    <!-- 新增弹出框 -->
    <advert-add ref="addData"></advert-add>
    <advert-edit ref="editData"></advert-edit>
  </div>
</template>
<script>
import SiruiTable from "../../components/SiruiTable/index";
import AdvertAdd from "./AdvertAdd";
import AdvertEdit from "./AdvertEdit";
export default {
  components: {
    SiruiTable,
    AdvertAdd,
    AdvertEdit,
  },
  name: "AdvertManages",
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
      imgUrl: "item.imgUrl",
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
          lable: "广告编号",
          width: 100,
          num: 1,
        },
        {
          prop: "title",
          lable: "广告标题",
          width: 180,
          num: 2,
        },
        {
          prop: "content",
          lable: "广告内容",
          width: 180,
          num: 3,
        },

        {
          prop: "points",
          lable: "奖励积分",
          width: 150,
          num: 5,
        },
        {
          prop: "isEnable",
          lable: "广告状态",

          width: 100,
          num: 6,
        },
        {
          prop: "delFlag",
          lable: "删除状态",
          width: 100,
          num: 7,
        },
      ],
      page: {
        pages: 4,
        total: 0,
        pageSize: 10, //每页显示几条
        pageNum: 1, //第几页
      },
      tableData: [
        // {
        // 	"id": 1,
        //     "title": "校园卡",
        //     "content": "校园卡即将发布，尽情期待",
        //     "imgUrl": "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
        //     "points": 1000,
        //     "isEnable": 0,
        //     "sortNo": 1,
        //     "createTime": "2018-03-16 11:33:00",
        //     "createBy": "wangdong",
        //     "updateTime": "2018-03-16 11:33:00",
        //     "updateBy": "wangdong",
        //     "version": 2,
        //     "delFlag": 0
        // },
        // {
        //     "id": 2,
        //     "title": "图书卡",
        //     "content": "图书卡即将发布，尽情期待",
        //     "imgUrl": "",
        //     "points": 500, "isEnable": 0,
        //     "sortNo": 1,
        //     "createTime": "2018-03-16 11:33:00",
        //     "createBy": "wangdong",
        //     "updateTime": null,
        //     "updateBy": null,
        //     "version": 1,
        //     "delFlag": 0
        // },
      ],
      multipleSelection: [],
      ostatus: "",
    };
  },

  computed: {
    columnList1: function () {
      return this.columnList.slice(0, 3);
    },
    columnList2: function () {
      return this.columnList.slice(3, 6);
    },
  },

  mounted() {
    this.getData();
  },

  methods: {
    words() {
      for (let i = 0; i < this.tableData.length; i++) {
        if (this.tableData[i].isEnable == 1) {
          this.tableData[i].isEnable = "停用";
        } else if (this.tableData[i].isEnable == 0) {
          this.tableData[i].isEnable = "启用";
        }
        if (this.tableData[i].delFlag == 0) {
          this.tableData[i].delFlag = "未删除";
        } else if (this.tableData[i].delFlag == 1) {
          this.tableData[i].delFlag = "已删除";
        }
      }
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.getData();
    },

    search() {
      this.getData();
    },

    //查询数据列表
    getData() {
      this.$axios
        .get(
          `/backend/advertisement/list?pageNum=${this.page.pageNum}&pageSize=${this.page.pageSize}&searchValue=${this.imform.searchValue}`
        )
        .then((resp) => {
          console.log(resp);
          if (resp) {
            this.tableData = resp.data.rows;
            this.page.total = resp.data.total;
            this.page.pages = Math.round(resp.data.total / resp.data.pageSize);
            this.words();
            for (var i = 0; i < this.tableData.length; i++) {
              this.tableData[i].imgUrl =
                this.$store.state.url + this.tableData[i].imgUrl;
            }
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

    start() {
      if (this.multipleSelection.length >= 1) {
        this.$confirm("是否启用", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let params = {
              ids: [],
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.ids.push(this.multipleSelection[i].id);
            }
            params.ids = params.ids.join(",");
            this.ostatus = "enable";
            this.$axios
              .put("backend/advertisement/" + this.ostatus + "/" + params.ids)
              .then((resp) => {
                if (200 == resp.data.code) {
                  console.log(resp);
                  this.words();
                  this.search();
                  this.$message({
                    showClose: true,
                    message: resp.data.msg,
                    type: "success",
                  });
                } else {
                  this.$message({
                    showClose: true,
                    message: resp.data.msg,
                    type: "error",
                  });
                }
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消启用",
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作",
        });
      }
    },
    forbid() {
      if (this.multipleSelection.length >= 1) {
        this.$confirm("是否停用", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let params = {
              ids: [],
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.ids.push(this.multipleSelection[i].id);
            }
            params.ids = params.ids.join(",");
            this.ostatus = "disable";
            this.$axios
              .put("backend/advertisement/" + this.ostatus + "/" + params.ids)
              .then((resp) => {
                if (200 == resp.data.code) {
                  console.log(resp);
                  this.words();
                  this.search();
                  this.$message({
                    showClose: true,
                    message: resp.data.msg,
                    type: "success",
                  });
                } else {
                  this.$message({
                    showClose: true,
                    message: resp.data.msg,
                    type: "error",
                  });
                }
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消停用",
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作",
        });
      }
    },
    Delete() {
      if (this.multipleSelection.length >= 1) {
        this.$confirm("是否删除", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            let params = {
              ids: [],
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.ids.push(this.multipleSelection[i].id);
            }
            params.ids = params.ids.join(",");
            this.$axios
              .delete("backend/advertisement/delete/" + params.ids)
              .then((resp) => {
                if (200 == resp.data.code) {
                  console.log(resp);
                  this.words();
                  this.search();
                  this.$message({
                    showClose: true,
                    message: resp.data.msg,
                    type: "success",
                  });
                } else {
                  this.$message({
                    showClose: true,
                    message: resp.data.msg,
                    type: "error",
                  });
                }
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作",
        });
      }
    },
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
