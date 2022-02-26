<template>
	<div class="wrapper">
		<v-sider></v-sider>
		<div class="content-box">
			<v-head></v-head>
			<div class="app-wrap">
				<div class="content">
					
					<router-view></router-view>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
	import vHead from "./Header";
	import vSider from "./Sider.vue";
	
	export default {
		name: "HomePage",
		components: {
			vHead,
			vSider,
		},
	};
</script>

<style scoped>
	.content {
		position: absolute;
	}
</style>>
