import {computed, ref} from 'vue'
import {defineStore} from 'pinia'
import type {Project, Skill} from '@/types'
import axiosInstance from '@/axiosInstance.ts'

const staticProjects: Project[] = [
	{
		id: -1,
		title: 'Rewatch',
		description: 'A watchlist tracker for movies, anime, K-dramas and TV series. Search titles with poster suggestions powered by TMDB, and keep track of everything you have watched.',
		techStack: 'Vue, TypeScript, TMDB API',
		repoUrl: null,
		liveUrl: 'https://rewatch-web.vercel.app/',
		imageUrl: 'https://s0.wp.com/mshots/v1/https%3A%2F%2Frewatch-web.vercel.app%2F?w=1200&h=675',
		featured: true,
		createDate: new Date().toISOString()
	}
]

export const usePortfolioStore = defineStore('portfolio', () => {
	const projects = ref<Project[]>([])
	const skills = ref<Skill[]>([])
	const loading = ref(false)

	const skillsByCategory = computed(() => {
		const groups: Record<string, Skill[]> = {}
		for (const skill of skills.value) {
			(groups[skill.category] ??= []).push(skill)
		}
		return groups
	})

	const loadProjects = async () => {
		const {data} = await axiosInstance.get<Project[]>('project/')
		projects.value = [...staticProjects, ...data]
	}

	const loadSkills = async () => {
		const {data} = await axiosInstance.get<Skill[]>('skill/')
		skills.value = data
	}

	const loadAll = async () => {
		loading.value = true
		try {
			await Promise.all([loadProjects(), loadSkills()])
		} catch (error) {
			console.error('Failed to load portfolio data:', error)
			projects.value = staticProjects
		} finally {
			loading.value = false
		}
	}

	return {projects, skills, loading, skillsByCategory, loadProjects, loadSkills, loadAll}
})