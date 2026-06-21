

console.log("Constant Backend : " + import.meta.env.BACKEND_API_URL);

export const AppConstants = {
  APP_NAME: '',
  API_BASE_URL_LOCAL: 'http://localhost:8080/api/v1.0',
  API_BASE_URL: import.meta.env.VITE_BACKEND_API_URL
}