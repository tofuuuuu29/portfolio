<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import axiosInstance from '@/axiosInstance.ts'
import {useAuthStore} from '@/stores/auth.ts'
import {usePortfolioStore} from '@/stores/portfolio.ts'
import type {Message, ProjectRequest} from '@/types'

const router = useRouter()
const authStore = useAuthStore()
const portfolio = usePortfolioStore()

const tab = ref<'projects' | 'skills' | 'messages'>('projects')
const messages = ref<Message[]>([])

const emptyProject = (): ProjectRequest => ({
	title: '', description: '', techStack: '', repoUrl: '', liveUrl: '', imageUrl: '', featured: false
})
const projectForm = reactive<ProjectRequest>(emptyProject())
const skillForm = reactive({name: '', category: 'General'})

const loadMessages = async () => {
	const {data} = await axiosInstance.get<Message[]>('message/')
	messages.value = data
}

onMounted(async () => {
	await portfolio.loadAll()
	await loadMessages()
})

const addProject = async () => {
	await axiosInstance.post('project/', projectForm)
	Object.assign(projectForm, emptyProject())
	await portfolio.loadProjects()
}

const removeProject = async (id: number) => {
	await axiosInstance.delete(`project/${id}/`)
	await portfolio.loadProjects()
}

const addSkill = async () => {
	await axiosInstance.post('skill/', skillForm)
	skillForm.name = ''
	await portfolio.loadSkills()
}

const removeSkill = async (id: number) => {
	await axiosInstance.delete(`skill/${id}/`)
	await portfolio.loadSkills()
}

const removeMessage = async (id: number) => {
	await axiosInstance.delete(`message/${id}/`)
	await loadMessages()
}

const logout = () => {
	authStore.logout()
	router.push({name: 'login'})
}

const input = 'w-full rounded-lg border border-white/15 bg-transparent px-3 py-2 outline-none focus:border-accent'
const btn = 'rounded-full bg-accent px-4 py-2 text-sm font-medium text-ink'
</script>

<template>
	<div class="min-h-screen bg-ink text-paper">
		<header class="flex items-center justify-between border-b border-white/10 px-6 py-4">
			<h1 class="font-semibold">Dashboard <span class="text-white/40">({{ authStore.account?.username }})</span></h1>
			<div class="flex gap-4 text-sm">
				<RouterLink to="/" class="text-white/60 hover:text-white">View site</RouterLink>
				<button class="text-white/60 hover:text-white" @click="logout">Log out</button>
			</div>
		</header>

		<main class="mx-auto max-w-4xl px-6 py-10">
			<nav class="mb-8 flex gap-2">
				<button v-for="t in (['projects', 'skills', 'messages'] as const)" :key="t"
						class="rounded-full px-4 py-1.5 text-sm capitalize"
						:class="tab === t ? 'bg-accent text-ink' : 'border border-white/15'"
						@click="tab = t">{{ t }}</button>
			</nav>

			<!-- Projects -->
			<section v-if="tab === 'projects'">
				<form class="mb-10 grid gap-3 md:grid-cols-2" @submit.prevent="addProject">
					<input v-model="projectForm.title" :class="input" placeholder="Title" required />
					<input v-model="projectForm.techStack" :class="input" placeholder="Tech stack (Kotlin, Vue, ...)" />
					<input v-model="projectForm.repoUrl" :class="input" placeholder="Repo URL" />
					<input v-model="projectForm.liveUrl" :class="input" placeholder="Live URL" />
					<input v-model="projectForm.imageUrl" :class="input + ' md:col-span-2'" placeholder="Image URL" />
					<textarea v-model="projectForm.description" :class="input + ' md:col-span-2'" rows="3"
							  placeholder="Description" required></textarea>
					<label class="flex items-center gap-2 text-sm">
						<input v-model="projectForm.featured" type="checkbox" /> Featured
					</label>
					<button type="submit" :class="btn + ' justify-self-end'">Add project</button>
				</form>

				<ul class="divide-y divide-white/10">
					<li v-for="p in portfolio.projects" :key="p.id" class="flex items-center justify-between py-3">
						<span>{{ p.title }} <span v-if="p.featured" class="text-xs text-accent">(featured)</span></span>
						<button class="text-sm text-red-400" @click="removeProject(p.id)">Delete</button>
					</li>
				</ul>
			</section>

			<!-- Skills -->
			<section v-if="tab === 'skills'">
				<form class="mb-10 flex gap-3" @submit.prevent="addSkill">
					<input v-model="skillForm.name" :class="input" placeholder="Skill" required />
					<input v-model="skillForm.category" :class="input" placeholder="Category" />
					<button type="submit" :class="btn">Add</button>
				</form>

				<ul class="divide-y divide-white/10">
					<li v-for="s in portfolio.skills" :key="s.id" class="flex items-center justify-between py-3">
						<span>{{ s.name }} <span class="text-xs text-white/40">{{ s.category }}</span></span>
						<button class="text-sm text-red-400" @click="removeSkill(s.id)">Delete</button>
					</li>
				</ul>
			</section>

			<!-- Messages -->
			<section v-if="tab === 'messages'">
				<p v-if="!messages.length" class="text-white/50">No messages yet.</p>
				<ul class="space-y-4">
					<li v-for="m in messages" :key="m.id" class="rounded-xl border border-white/10 p-4">
						<div class="flex items-start justify-between">
							<div>
								<p class="font-medium">{{ m.name }}</p>
								<a :href="`mailto:${m.email}`" class="text-sm text-accent">{{ m.email }}</a>
							</div>
							<button class="text-sm text-red-400" @click="removeMessage(m.id)">Delete</button>
						</div>
						<p class="mt-3 whitespace-pre-wrap text-white/70">{{ m.content }}</p>
						<p class="mt-2 text-xs text-white/30">{{ new Date(m.createDate).toLocaleString() }}</p>
					</li>
				</ul>
			</section>
		</main>
	</div>
</template>
