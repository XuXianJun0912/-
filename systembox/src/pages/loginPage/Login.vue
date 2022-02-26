<template>
  <div class="app">
    <div class="logo">
      <img src="..\..\assets\logo.png" alt="" />
    </div>

    <div class="cent">
      <div class="login">
        <div class="login_left l">
          <p>储留箱后台管理系统</p>
          <p>STORAGE BOX BACKGROUND MANAGEMENT SYSTEM</p>
        </div>

        <div class="login_right l">
          <p>欢迎登陆</p>
          <p>储留箱后台管理系统</p>

          <div class="ele">
            <el-form :model="form" status-icon :rules="rules" ref="form">
              <el-form-item prop="username">
                <div class="box">
                  <el-input placeholder="请输入用户名" v-model="form.username">
                    <template slot="prepend">
                      <img src="../../assets/yonghu-2.png" alt="" />
                    </template>
                  </el-input>
                </div>
              </el-form-item>
              <el-form-item prop="password">
                <div class="box">
                  <el-input
                    placeholder="请输入密码"
                    v-model="form.password"
                    show-password
                  >
                    <template slot="prepend">
                      <img src="../../assets/mima-2.png" alt="" />
                    </template>
                  </el-input>
                </div>
              </el-form-item>
              <div class="login-btn">
                <el-button type="primary" @click="submitForm('form')"
                  >登录</el-button
                >
              </div>
            </el-form>
          </div>

          <p class="wangjimima">
            <a href="#" @click="help">忘记密码？寻求帮助</a>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    var checkUserName = (rule, value, callback) => {
      var userName = /^[a-zA-Z][a-zA-Z0-9]{2,15}$/;
      var idCard = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
      var phone = /^1[3456789]\d{9}$/;
      if (!value) {
        callback(new Error("请输入用户名/手机号/身份证号"));
      }
      setTimeout(() => {
        if (
          !idCard.test(value) &&
          !userName.test(value) &&
          !phone.test(value)
        ) {
          callback(new Error("请输入正确的用户名/手机号/邮箱"));
        } else {
          callback();
        }
      }, 100);
    };
    return {
      userType: "01",
      form: {
        username: "",
        password: "",
      },
      loginUrl: this.$store.state.url + "/common/login",
      getLoginInfo: this.$store.state.url + "/common/getLoginInfo",
      role: "",
      rules: {
        username: [
          { required: true, validator: checkUserName, trigger: "blur" },
        ],
        password: [
          {
            required: true,
            // pattern: /^[a-zA-Z0-9]{3,15}$/,
            message: "请输入正确的密码",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    // 清除登录人信息
    localStorage.clear();
  },
  methods: {
    
    // 获取登录人信息接口
    loginPerson() {
      let PersonParms = {};
      this.$axios.get(this.getLoginInfo, PersonParms).then((res) => {
        if (200 == res.data.code) {
          this.role = res.data.roles[0];
          if (this.role == "app") {
            this.$router.push("/");
            this.$message({
              showClose: true,
              message: "用户为手机端用户，无法登陆",
              type: "error",
            });
          } else if (this.role == "admin") {
            this.$router.push("homepage");
            this.$message({
              showClose: true,
              message: "用户为测试用户，无法登陆",
              type: "error",
            });
          }
          this.$message({
            showClose: true,
            message: res.data.msg,
            type: "success",
          });
        } else if (500 == res.data.code) {
          this.$message({
            showClose: true,
            message: res.data.msg,
            type: "error",
          });
        }
      });
    },

    // 忘记密码
    // help() {
    //   this.$alert("请联系管理员13234567821", "忘记密码", {
    //     confirmButtonText: "确定",
    //     callback: (action) => {
    //       this.$message({
    //         type: "info",
    //         message: `action: ${action}`,
    //       });
    //     },
    //   });
    // },
    help(){
      alert("忘记密码，请联系管理员13234567821")
      // this.$confirm("请联系管理员13234567821", "忘记密码", {
      //     confirmButtonText: "确定",
      //     cancelButtonText: "取消",
      //     type: "info",
      //   })
    },

    // 登录
    submitForm(formName) {
      console.log(this.$refs[formName].validate);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // console.log(valid);
          let paramForm = {
            username: this.form.username,
            password: this.form.password,
            userType: this.userType,
          };
          this.$axios.post(this.loginUrl, paramForm).then((res) => {
            if (200 == res.data.code) {
              this.loginPerson();
              console.log(res.data);
              localStorage.setItem("username", paramForm.username);
              localStorage.setItem("token", res.data.token); //存返回的token值
              this.$router.push("ordermanages");
              this.$message({
                showClose: true,
                message: res.data.msg,
                type: "success",
              });
            } else if (500 == res.data.code) {
              this.$message({
                showClose: true,
                message: res.data.msg,
                type: "error",
              });

              this.form.username = "";
              this.form.password = "";
            }
          });
        }
      });
    },
  },
};
</script>

<style scoped>
/* reset部分 */
body,
h1,
h2,
h3,
h4,
p,
ol,
ul,
li,
a,
p,
span,
img,
input,
dl,
dt,
dd {
  padding: 0;
  margin: 0;
}

ul,
ol {
  list-style: none;
}

img {
  display: block;
  border: 0;
}

input {
  outline: none;
}

body {
  font-size: 12px;
  color: #666;
  font-family: "微软雅黑";
}

a {
  color: black;
  text-decoration: none;
}

a:hover {
  color: #53cac3;
}

/* public部分 */

.w {
  width: 1170px;
  margin: 0 auto;
}

.l {
  float: left;
}

.r {
  float: right;
}

/* app部分 */

.app {
  width: 100%;
  height: 800px;
  background-image: url(../../assets/bg2.png);
}

/* logo部分 */

.logo {
  width: 100%;
  height: 55px;
}

.logo img {
  width: 87px;
  height: 53px;
}

/* cent部分 */

.cent {
  width: 800px;
  height: 350px;
  margin: 100px auto;
}

.cent .login .login_left {
  width: 399px;
  height: 350px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.1);
}

.cent .login .login_right {
  width: 399px;
  height: 350px;
  background: white;
  border-radius: 10px;
  margin-left: 2px;
}

.cent .login .login_left p:nth-child(1) {
  text-align: center;
  font-size: 28px;
  font-weight: 700;
  color: white;
  margin-top: 70px;
}

.cent .login .login_left p:nth-child(2) {
  text-align: center;
  font-size: 14px;
  font-weight: 500;
  color: white;
  margin-top: 30px;
}

.cent .login .login_right p:nth-child(1) {
  padding-top: 40px;
  padding-left: 50px;
  font-size: 24px;
  color: black;
}

.cent .login .login_right p:nth-child(2) {
  padding-top: 10px;
  padding-left: 50px;
  font-size: 20px;
  color: gray;
}

.cent .login .login_right .box {
  width: 230px;
  height: 40px;
}

.cent .login .login_right .box:nth-child(1) {
  margin-top: 15px;
}

.cent .login .login_right .wangjimima {
  width: 150px;
  margin: 5px auto;
  font-size: 14px;
}

.cent .login .login_right .login-btn {
  width: 60px;
  height: 40px;
  margin-left: 80px;
  margin-top: 25px;
}

.cent .login .login_right .ele {
  margin-left: 80px;
}
</style>