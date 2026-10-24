export interface Message {
	id: number
	name: string
	email: string
	content: string
	createDate: string
}

export type MessageRequest = Pick<Message, 'name' | 'email' | 'content'>
