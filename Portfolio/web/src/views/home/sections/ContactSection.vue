<script setup lang="ts">
import {onBeforeUnmount, reactive, ref} from 'vue'
import axiosInstance from '@/axiosInstance.ts'
import {site} from '@/config/site.ts'
import type {MessageRequest} from '@/types'

const form = reactive<MessageRequest>({name: '', email: '', content: ''})
const status = ref<'idle' | 'sending' | 'sent' | 'error'>('idle')

const submit = async () => {
	status.value = 'sending'
	try {
		await axiosInstance.post('message/', form)
		form.name = form.email = form.content = ''
		status.value = 'sent'
	} catch {
		status.value = 'error'
	}
}

// --- copy email to clipboard, with a quick "Copied!" tooltip ---
const copied = ref(false)
let copyTimer: ReturnType<typeof setTimeout> | undefined

const copyEmail = async () => {
	try {
		await navigator.clipboard.writeText(site.email)
		copied.value = true
		clearTimeout(copyTimer)
		copyTimer = setTimeout(() => (copied.value = false), 1800)
	} catch {
		// clipboard blocked: the mailto link still works
	}
}
onBeforeUnmount(() => clearTimeout(copyTimer))

const field = 'w-full rounded-xl border border-white/15 bg-transparent px-4 py-3 outline-none transition focus:border-accent focus:shadow-[0_0_0_3px_rgba(42,159,214,0.15)]'
</script>

<template>
	<section id="contact" class="mx-auto max-w-3xl px-6 py-24">
		<h2 class="mb-4 text-3xl font-semibold tracking-tight">Let's talk</h2>
		<p class="mb-10 text-white/60">
			Send a message or email me at
			<a :href="`mailto:${site.email}`" class="text-accent hover:underline">{{ site.email }}</a>.
			<span class="relative ml-1 inline-block align-middle">
				<button type="button" aria-label="Copy email address" @click="copyEmail"
						class="text-lg text-white/50 transition hover:text-accent active:scale-90">
					<i :class="['bx', copied ? 'bx-check' : 'bx-copy']"></i>
				</button>
				<Transition enter-active-class="transition duration-200" leave-active-class="transition duration-200"
							enter-from-class="translate-y-1 opacity-0" leave-to-class="translate-y-1 opacity-0">
					<span v-if="copied"
						  class="pointer-events-none absolute -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap rounded-md bg-accent px-2 py-1 text-xs text-ink">
						Copied!
					</span>
				</Transition>
			</span>
		</p>

		<div class="mb-10 flex gap-4">
			<a v-for="s in site.socials" :key="s.url" :href="s.url" target="_blank" rel="noopener"
			   class="text-2xl text-white/60 transition hover:-translate-y-1 hover:text-accent" :aria-label="s.label">
				<i :class="['bx', s.icon]"></i>
			</a>
		</div>

		<form class="space-y-4" @submit.prevent="submit">
			<input v-model="form.name" :class="field" placeholder="Your name" required />
			<input v-model="form.email" :class="field" type="email" placeholder="Your email" required />
			<textarea v-model="form.content" :class="field" rows="5" placeholder="Message" required></textarea>
			<button type="submit" :disabled="status === 'sending'"
					class="group rounded-full bg-accent px-6 py-3 font-medium text-ink transition hover:opacity-90 active:scale-95 disabled:opacity-50">
				{{ status === 'sending' ? 'Sending...' : 'Send message' }}
				<i v-if="status !== 'sending'" class="bx bx-right-arrow-alt ml-1 inline-block align-middle transition-transform duration-300 group-hover:translate-x-1"></i>
			</button>
			<p v-if="status === 'sent'" class="text-sm text-accent">Thanks! I'll get back to you soon.</p>
			<p v-if="status === 'error'" class="text-sm text-red-400">Something went wrong. Please try again.</p>
		</form>
	</section>
</template>