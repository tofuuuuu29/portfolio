import type {Directive} from 'vue'

// usage: v-reveal  or  v-reveal="150"  (delay in ms)
export const reveal: Directive<HTMLElement, number | undefined> = {
	mounted(el, binding) {
		el.classList.add('reveal')
		el.style.transitionDelay = `${binding.value ?? 0}ms`

		const io = new IntersectionObserver(([entry]) => {
			if (!entry.isIntersecting) return
			el.classList.add('reveal-in')
			io.disconnect()
		}, {threshold: 0.15})
		io.observe(el)

		// clean up afterwards so it doesn't delay your hover effects
		el.addEventListener('transitionend', (e) => {
			if (e.propertyName !== 'opacity') return
			el.classList.remove('reveal', 'reveal-in')
			el.style.transitionDelay = ''
		})
	}
}