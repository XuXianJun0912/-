<template>
  <div class="login">
    <div class="top">
      <!-- <img width="100" height="100" src="../assets/bg1.png" class="bg" /> -->
      <label for="储留箱" class="tit">储留箱</label>
    </div>
    <div class="inp">
      <van-form class="onSubmit">
        <van-field
          v-model="username"
          name="用户名"
          placeholder=" 用户名"
          left-icon="manager"
        />
        <van-field
          v-model="password"
          type="password"
          name="密码"
          placeholder=" 密码"
          left-icon="eye"
        />
        <div class="btn">
          <van-button
            round
            block
            type="info"
            native-type="submit"
            @click="toLogin()"
            >登录</van-button
          >
        </div>
      </van-form>
    </div>
    <div class="bott">
      <div class="forgetPassword">忘记密码</div>
      <div class="register" @click="register()">注册</div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      password: "",
      username: "",
      userType: "02",
      role: "",
    };
  },
  mounted() {
    // 清除登录人信息
    this.loginPerson();
    localStorage.clear();
  },
  beforeDestroy() {
    console.log("updated后" + this.role);
  },
  methods: {
    // 跳转注册页
    register() {
      this.$router.push("/register");
    },
    // 获取登录人信息接口
    loginPerson() {
      let PersonParms = {};

      this.$axios
        .get("/common/getLoginInfo", PersonParms)
        .then((res) => {
          if (200 == res.data.code) {
            this.$toast.success(res.data.msg);
            console.log(res.data.roles[0]);
            this.role = res.data.roles[0];
            localStorage.setItem("roles", this.role);
            console.log("存储的roles" + localStorage.roles);
            console.log(this.role);
            if (this.role == "app") {
            } else if (this.role == "backend") {
              this.$router.push("/");
              this.$toast.fail("该用户为PC端用户");
            }
          } else if (500 == res.data.code) {
            this.$toast.success(res.data.msg);
          }
        });
    },
    // 校验输入项
    toLogin() {
      var username = /^[a-zA-Z][a-zA-Z0-9]{3,15}$/;
      var idCard = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
      var phone = /^1[3456789]\d{9}$/;
      var password = /^[a-zA-Z0-9]{3,15}$/;
      if (!this.username) {
        this.$toast.fail("用户名不能为空");
      } else if (
        !username.test(this.username) &&
        !idCard.test(this.username) &&
        !phone.test(this.username) &&
        !password.test(this.password)
      ) {
        this.$toast.fail("用户名或密码错误");
        this.username = "";
        this.password = "";
      } else if (!this.password) {
        this.$toast.fail("密码不能为空");
      } else {
        let paramForm = {
          username: this.username,
          password: this.password,
          userType: this.userType,
        };
        this.$axios.post("/common/login", paramForm).then((res) => {
          if (res.data.code == 200) {
            // 调用登录人信息函数

            this.loginPerson();
            // setTimeout(function() {
            //   self.refreshBtn = false;
            // }, 2000);
            this.$toast.success(res.data.msg);

            localStorage.setItem("username", paramForm.username);

            localStorage.setItem("token", res.data.token); //存返回的token值

            // this.$toast.success(res.data.msg);
            this.$router.push("/index");

            console.log("role" + this.role);
          } else {
            this.$toast.fail(res.data.msg);
          }
        });
      }
    },
  },
};
</script>
<style scoped>
.login {
  width: 100%;
  background: white;
  margin: auto;
  padding-bottom: 55px;
}
.top {
  width: 100%;
  height: 200px;
  line-height: 200px;
  background-image: url("../assets/bg1.png");
  text-align: center;
}

.tit {
  font-size: 50px;
  color: white;
  font-weight: bold;
}
.inp {
  width: 90%;
  height: auto;
  margin: 0px auto;
  margin-bottom: 50px;
}
.btn {
  margin-top: 40px;
}
.van-cell {
  height: 70px;
  color: #046ac6;
  padding-top: 30px;
}
.bott {
  background: white;
  width: 100%;
  height: 50px;
  line-height: 50px;
  display: flex;
  justify-content: space-around;
}
.onSubmit {
  margin-top: 80px;
}
.forgetPassword {
  color: rgb(80, 75, 75);
}
.register {
  color: rgb(80, 75, 75);
}
</style>