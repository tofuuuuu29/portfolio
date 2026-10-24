<script setup lang="ts">
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {useAuthStore} from '@/stores/auth.ts'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

const submit = async () => {
	error.value = ''
	loading.value = true
	try {
		await authStore.login(username.value, password.value)
		await router.push({name: 'dashboard'})
	} catch {
		error.value = 'Invalid username or password.'
	} finally {
		loading.value = false
	}
}
</script>

<template>
	<div class="flex min-h-screen items-center justify-center bg-ink px-6 text-paper">
		<form class="w-full max-w-sm space-y-4" @submit.prevent="submit">
			<h1 class="mb-6 text-2xl font-semibold">Admin login</h1>
			<input v-model="username" placeholder="Username" required
				   class="w-full rounded-xl border border-white/15 bg-transparent px-4 py-3 outline-none focus:border-accent" />
			<input v-model="password" type="password" placeholder="Password" required
				   class="w-full rounded-xl border border-white/15 bg-transparent px-4 py-3 outline-none focus:border-accent" />
			<p v-if="error" class="text-sm text-red-400">{{ error }}</p>
			<button type="submit" :disabled="loading"
					class="w-full rounded-full bg-accent px-6 py-3 font-medium text-ink disabled:opacity-50">
				{{ loading ? 'Signing in...' : 'Sign in' }}
			</button>
			<RouterLink to="/" class="block text-center text-sm text-white/40 hover:text-white/70">Back to site</RouterLink>
		</form>
	</div>
</template>
