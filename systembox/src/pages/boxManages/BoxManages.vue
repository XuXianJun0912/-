<template>
  <div class="user">
    <!-- 查询箱子 -->
    <el-card class="box-card">
      <el-form
        :model="imform"
        ref="imform"
        label-width="100px"
        :rules="rule1"
        :inline="true"
      >
        <el-form-item label="箱子规格" prop="searchValue" class="searchValue">
          <!-- <el-input
            v-model="imform.searchValue"
            class="nameBtn"
            placeholder="请选择"
          ></el-input> -->

          <el-select v-model="imform.searchValue" placeholder="请选择">
            <el-option
              v-for="item in tableData"
              :key="item.id"
              :label="item.boxStandard"
              :value="item.boxStandard"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="search()" class="addBtn"
        >查询</el-button
      >
      <el-button @click="resetForm('imform')" class="resetBtn">重置</el-button>
    </el-card>

    <!--箱子列表-->
    <el-card>
      <el-button type="primary" @click="addData()">新增</el-button>
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
          v-for="item in columnList"
          :key="item.num"
          :label="item.lable"
          :width="item.width"
          :prop="item.prop"
        ></el-table-column>
        <el-table-column prop="useRatio" label="使用比例" width="200">
          <template slot-scope="scope">
            <el-progress
              :percentage="parseFloat(scope.row.useRatio)"
              :status="scope.row.status"
            >
            </el-progress>
          </template>
        </el-table-column>
      </sirui-table>
    </el-card>

    <!-- 新增弹出框 -->
    <box-add ref="addData"></box-add>
  </div>
</template>
<script>
import SiruiTable from "../../components/SiruiTable/index";
import BoxAdd from "./BoxAdd";

export default {
  components: {
    SiruiTable,
    BoxAdd,
  },
  name: "BoxManages",
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
          prop: "boxStandard",
          lable: "箱子规格",
          width: 150,
          num: 1,
        },
        {
          prop: "totalNumber",
          lable: "总数量",
          width: 150,
          num: 2,
        },
        {
          prop: "usedNumber",
          lable: "已用数量",
          width: 150,
          num: 3,
        },
        {
          prop: "inventoryNumber",
          lable: "库存",
          width: 150,
          num: 4,
        },

        {
          prop: "boxUnitPrice",
          lable: "所需积分",
          width: 150,
          num: 6,
        },
        // {
        //   prop: "useRatio",
        //   lable: "使用比例",
        //   width: 150,
        //   num: 5,
        // },
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
          `/backend/box/standard/list?pageNum=${this.page.pageNum}&pageSize=${this.page.pageSize}&searchValue=${this.imform.searchValue}`
        )
        .then((resp) => {
          console.log(resp);
          if (resp) {
            if (200 == resp.data.code) {
              this.tableData = resp.data.rows;
              this.page.total = resp.data.total;
              this.page.pages = Math.round(
                resp.data.total / resp.data.pageSize
              );
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
    // editData() {
    //   //debugger;
    //   if (1 == this.multipleSelection.length) {
    //     this.$refs.editData.openDialog(
    //       true,
    //       JSON.parse(JSON.stringify(this.multipleSelection))
    //     );
    //   } else if (this.multipleSelection.length > 1) {
    //     this.$message({
    //       type: "info",
    //       message: "请选择一条数据进行操作",
    //     });
    //   } else {
    //     this.$message({ type: "info", message: "请选择数据进行操作" });
    //   }
    // },

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
              .delete("backend/box/standard/delete/" + params.ids)
              .then((resp) => {
                if (200 == resp.data.code) {
                  console.log(resp);
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
