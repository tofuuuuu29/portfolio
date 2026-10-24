export interface Account {
	id: number
	username: string
	role: string
}

export interface LoginResponse {
	accessToken: string
	refreshToken: string
	account: Account
}
