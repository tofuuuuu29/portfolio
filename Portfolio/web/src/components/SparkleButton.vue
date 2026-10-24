<script setup lang="ts">
defineProps<{ to: string }>()
</script>

<template>
	<RouterLink :to="to" class="sparkle-btn">
		<span><slot/></span>
		<svg v-for="n in 5" :key="n" :class="['spark', `spark-${n}`]" viewBox="0 0 24 24" aria-hidden="true">
			<path d="M12 0 L14.5 9.5 L24 12 L14.5 14.5 L12 24 L9.5 14.5 L0 12 L9.5 9.5 Z"/>
		</svg>
	</RouterLink>
</template>