<script setup lang="ts">
import {onBeforeUnmount, onMounted, ref} from 'vue'

const visible = ref(false)

const onScroll = () => {
	// show once the hero is mostly scrolled past
	visible.value = window.scrollY > window.innerHeight * 0.8
}

const toTop = () => window.scrollTo({top: 0, behavior: 'smooth'})

onMounted(() => {
	window.addEventListener('scroll', onScroll, {passive: true})
	onScroll()
})
onBeforeUnmount(() => window.removeEventListener('scroll', onScroll))
</script>

<template>
	<Transition enter-active-class="transition duration-300" leave-active-class="transition duration-200"
				enter-from-class="translate-y-4 opacity-0" leave-to-class="translate-y-4 opacity-0">
		<button v-if="visible" type="button" aria-label="Back to top" @click="toTop"
				class="group fixed bottom-5 right-5 z-40 flex h-11 w-11 items-center justify-center rounded-full border border-white/20 bg-black/50 text-xl text-white backdrop-blur transition hover:border-accent hover:text-accent active:scale-90 sm:bottom-8 sm:right-8">
			<i class="bx bx-up-arrow-alt transition-transform duration-300 group-hover:-translate-y-0.5"></i>
		</button>
	</Transition>
</template>