<template>
  <!-- 导航栏 -->
  <div>
    <van-nav-bar
      fixed
      title="首页"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
    />

    <!-- 首页海报 -->

    <!-- 轮播图 -->
    <van-swipe
      class="my-swipe"
      :autoplay="3000"
      indicator-color="white"
      :lazy-render="true"
    >
      <van-swipe-item
        v-for="(image, index) in imgData"
        :key="index"
        @click="getPoints"
      >
        <img v-lazy="image" />
      </van-swipe-item>
    </van-swipe>

    <!-- 首页选项 -->
    <div class="box">
      <div class="person">
        <img src="../assets/person.png" @click="myCenter" />
        <p>个人中心</p>
      </div>
      <div class="nullBox">
        <img src="../assets/boxnull.png" @click="boxOrder" />
        <p>空箱预约</p>
      </div>
      <div class="list">
        <img src="../assets/list.png" @click="myOrder" />
        <p>我的订单</p>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      imgData: [],
      advertPoints: [],
    };
  },

  mounted() {
    this.getData();
    // this.loginPerson();
  },
  methods: {
    loginPerson() {
      console.log(localStorage.roles);
      // if (localStorage.roles == "app") {
      // } else if (localStorage.roles == "backend") {
      //   this.$router.push("/");
      //   this.$toast.fail("用户人为PC端！");
      // }
    },

    getData() {
      this.$axios.get("/app/advertisement/list").then((res) => {
        if (res.data.code == 200) {
          for (var i = 0; i < res.data.data.length; i++) {
            this.imgData.push(
              "http://localhost:8081" + res.data.data[i].imgUrl
            );
          }
          console.log(this.imgData);
          this.advertPoints = res.data.data;
          // console.log(this.advertPoints);
        }
      });
    },

    // 获取积分
    getPoints(row) {
      var imgSrc = row.path[0].src;
      console.log(imgSrc);
      if (imgSrc != undefined) {
        var imgSrc = row.path[0].src.split("http://localhost:8081");
        console.log(imgSrc);

        var id = "";
        var points = "";
        // console.log(imgSrc[1]);
        for (var i = 0; i < this.advertPoints.length; i++) {
          // console.log(this.advertPoints[i].imgUrl);

          if (imgSrc[1] == this.advertPoints[i].imgUrl) {
            id = this.advertPoints[i].id;
            points = this.advertPoints[i].points;
            // console.log("爱爱" + id);
            // console.log("婆婆" + points);
          }
        }
      }

      let params = {
        points: points,
        id: id,
      };

      // console.log(points);
      this.$axios.put("/app/advertisement/points", params).then((res) => {
        // console.log(res);
        if (200 == res.data.code) {
          this.$toast.success(res.data.msg);
          this.$router.push("/advertdetail");
          localStorage.setItem("advertId", id);
        }
      });
    },
    onClickLeft() {
      this.$router.push("/");
    },
    myCenter() {
      this.$router.push("/personcenter");
    },
    boxOrder() {
      this.$router.push("/nullbox");
    },
    myOrder() {
      this.$router.push("/orderlist");
    },
  },
};
</script>
<style scoped>
.box {
  position: relative;
  top: 46px;
  display: flex;
  justify-content: space-around;
  margin: 20px;
}
.my-swipe .van-swipe-item {
  color: #fff;
  font-size: 20px;
  height: 220px;
  line-height: 220px;
  text-align: center;
  background-color: #39a9ed;
}
.my-swipe .van-swipe-item img {
  width: 100%;
  height: 100%;
}
</style>