import {ref} from 'vue'

export type Theme = 'dark' | 'light'

const KEY = 'theme'

const read = (): Theme => {
	try {
		return localStorage.getItem(KEY) === 'light' ? 'light' : 'dark'
	} catch {
		return 'dark'
	}
}

const theme = ref<Theme>(read())

const apply = (t: Theme) => {
	document.documentElement.dataset.theme = t
}

apply(theme.value)

export const useTheme = () => {
	const toggle = () => {
		theme.value = theme.value === 'dark' ? 'light' : 'dark'
		apply(theme.value)
		try {
			localStorage.setItem(KEY, theme.value)
		} catch {
			/* storage blocked, ignore */
		}
	}
	return {theme, toggle}
}