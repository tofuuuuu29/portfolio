<script setup lang="ts">
import {usePortfolioStore} from '@/stores/portfolio.ts'
import BorderGlow from '@/components/BorderGlow.vue'

const portfolio = usePortfolioStore()
</script>

<template>
	<section id="skills" class="mx-auto max-w-6xl px-6 py-24">
		<h2 class="mb-12 text-3xl font-semibold tracking-tight">Skills</h2>

		<p v-if="!portfolio.skills.length" class="text-white/50">No skills yet.</p>

		<div class="grid gap-10 md:grid-cols-3">
			<div v-for="(items, category) in portfolio.skillsByCategory" :key="category">
				<h3 class="mb-4 text-sm uppercase tracking-widest text-white/50">{{ category }}</h3>
				<div class="flex flex-wrap gap-3">
					<BorderGlow v-for="skill in items" :key="skill.id" flat
								:border-radius="999" :glow-radius="14"
								:edge-sensitivity="40" :cone-spread="30">
						<span class="px-4 py-1.5 text-sm">{{ skill.name }}</span>
					</BorderGlow>
				</div>
			</div>
		</div>
	</section>
</template>