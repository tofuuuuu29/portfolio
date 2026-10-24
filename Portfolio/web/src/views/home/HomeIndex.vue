<script setup lang="ts">
import {onMounted} from 'vue'
import MainLayout from '@/layouts/MainLayout.vue'
import WaveBackground from '@/layouts/WaveBackground.vue'
import HeroSection from '@/views/home/sections/HeroSection.vue'
import AboutSection from '@/views/home/sections/AboutSection.vue'
import PortfolioSection from '@/views/home/sections/PortfolioSection.vue'
import ContactSection from '@/views/home/sections/ContactSection.vue'
import {usePortfolioStore} from '@/stores/portfolio.ts'
import IntroScreen from '@/components/IntroScreen.vue'

const portfolio = usePortfolioStore()

onMounted(() => portfolio.loadAll())
</script>

<template>
	<MainLayout>
		<IntroScreen />
		<WaveBackground />
		<HeroSection />
		<AboutSection />
		<PortfolioSection />
		<ContactSection />
	</MainLayout>
</template>