export interface Project {
	id: number
	title: string
	description: string
	techStack: string
	repoUrl?: string | null
	liveUrl?: string | null
	imageUrl?: string | null
	featured: boolean
	createDate: string
	updateDate?: string | null
}

export type ProjectRequest = Omit<Project, 'id' | 'createDate' | 'updateDate'>
