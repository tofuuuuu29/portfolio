<script setup lang="ts">
import Header from '@/layouts/Header.vue'
import BackToTop from '@/components/BackToTop.vue'
import {site} from '@/config/site.ts'
</script>

<template>
	<div class="min-h-screen text-paper">
		<Header />
		<main class="pt-16">
			<slot />
		</main>
		<footer class="border-t border-white/10 py-8 text-center text-sm text-white/50">
			<p>&copy; {{ new Date().getFullYear() }} {{ site.name }}</p>
			<RouterLink to="/login" class="mt-2 inline-block text-xs text-white/30 hover:text-white/60">Admin</RouterLink>
		</footer>
		<BackToTop />
	</div>
</template>