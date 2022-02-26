<template>
	<div class="header">
		<!-- 头部标题 -->
		<div class="systemTitle">储留箱后台管理系统</div>

		<div class="yonghu">
			<img src="../assets/头像.png" alt="" />
			<!-- 用户名下拉菜单 -->
			<el-dropdown @command="handleCommand" trigger="click">
				<span class="el-dropdown-link">
					{{ userName }}
					<i class="el-icon-arrow-down"></i>
				</span>

				<el-dropdown-menu slot="dropdown">
					<el-dropdown-item command="updatePw" @click="dialogVisible = true">
						修改密码
					</el-dropdown-item>
					<el-dropdown-item command="quiteLogin"> 退出登录 </el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>

			<!-- 修改密码框 -->
			<el-dialog title="修改密码" :visible.sync="dialogVisible" width="400px" :close-on-click-modal="false"
				@close="resetForm('ruleForm')">
				<el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="150px"
					class="demo-ruleForm">
					<el-form-item label="原密码" prop="oldPw">
						<el-input type="password" v-model="ruleForm.oldPw" autocomplete="off" width="10px"
							class="firPass"></el-input>
					</el-form-item>
					<el-form-item label="新密码" prop="newPw">
						<el-input type="password" v-model="ruleForm.newPw" autocomplete="off" width="10px"
							class="secPass"></el-input>
					</el-form-item>
				</el-form>
				<span slot="footer" class="dialog-footer">
					<el-button @click="resetForm('ruleForm')">取 消</el-button>
					<el-button type="primary" @click="sureForm('ruleForm')">确 定</el-button>
				</span>
			</el-dialog>
		</div>
	</div>
</template>

<script>
	export default {
		name: "Header",
		data() {
			var ValidatePass = (rule, value, callback) => {
				var oldPw = /^[a-zA-Z0-9](3,15)$/;

				if (value === "") {
					callback(new Error("请输入密码"));
				} else {
					if (!oldPw.test(value)) {
						callback(new Error("请输入正确的密码"));
					}
					callback();
				}
			};

			return {
				userName: "",
				dialogVisible: false,
				name: "xiaoming",
				ruleForm: {
					oldPw: "",
					newPw: "",
					age: "",
				},
				rules: {
					oldPw: [{
						required: true,
						validator: ValidatePass,
						trigger: "blur"
					}],
					newPw: [{
						required: true,
						validator: ValidatePass,
						trigger: "blur"
					}],
				},
			};
		},

		mounted(){
			this.userName = localStorage.getItem("username");
		},

		methods: {
			handleCommand(command) {
				if (command == "updatePw") {
					this.dialogVisible = true;
				}
				if (command == "quiteLogin") {
					this.$router.push("/");
				}
			},

			resetForm(formName) {
				this.dialogVisible = false;
				this.$refs[formName].resetFields();
			},


			

			
		},
	};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.header {
		position: absolute;
		box-sizing: border-box;
		top: 0;
		width: 100%;
		height: 60px;
		background-color: rgb(255, 255, 255);
		padding-left: 20px;
		border-bottom: 1px solid #dadfe7;
	}

	.systemTitle {
		color: #116dbf;
		font-size: 30px;
		line-height: 60px;
		font-weight: bold;
	}

	.yonghu {
		position: absolute;
		top: 5px;
		right: 10%;
	}

	.user_active {
		display: block;
		position: absolute;
		right: 60px;
		top: 0;
	}

	.el-dropdown-link {
		width: 70px;
		position: absolute;
		top: -45px;
	}

	.el-icon-arrow-down {
		position: absolute;
		top: 4px;
	}
</style>
