export interface Certificate {
	title: string
	issuer: string
	date: string
	image: string
	url: string
}

export const site = {
	firstName: 'Christian Gil ',
	lastName: 'German',
	name: 'Christian Gil M. German',
	role: 'Computer Engineering student',
	tagline: 'I turn ideas into working projects.',
	about: 'I am a Computer Engineering student who enjoys building things and learning how they work.\nI like turning ideas into working projects, from the hardware to the code, and I am always looking to learn something new.',
	email: 'christiangerman21@gmail.com',
	phone: '+63 915 815 9757', // change to your real number
	cvUrl: '/cv.pdf',
	location: 'Cagayan, Philippines',

	certificateList: [
		{
			title: 'Certificate of Participation: Regional ITE Summit 2026',
			issuer: 'St. Paul University Philippines',
			date: 'July 2026',
			image: '/certs/cert1.jpg',
			url: ''
		},
		{
			title: 'Basic Occupational Safety and Health Training',
			issuer: 'DOLE Occupational Safety and Health Center',
			date: 'June 2026',
			image: '/certs/cert2.jpg',
			url: ''
		}
	] as Certificate[],

	// shown in the Tech Stack tab; names below get a logo automatically
	techStack: [
		'Vue',
		'TypeScript',
		'JavaScript',
		'Tailwind',
		'HTML',
		'CSS',
		'Kotlin',
		'Spring Boot',
		'Python',
		'Django',
		'Arduino',
		'Git',
		'GitHub',
		'VS Code'
	] as string[],

	socials: [
		{label: 'GitHub', icon: 'bxl-github', url: 'https://github.com/tofuuuuu29'},
		{label: 'Facebook', icon: 'bxl-facebook', url: 'https://www.facebook.com/christian.german.146'},
		{label: 'Instagram', icon: 'bxl-instagram', url: 'https://instagram.com/your-name'}
	]
}