export function useCreateImageURL() {
  const urls: string[] = []

  function createUrl(file: File) {
    const urlObject = URL.createObjectURL(file)

    return {
      src: urlObject,
      alt: file.name,
    }
  }

  function removeUrl(url: string) {
    const index = urls.indexOf(url)
    if (index !== -1) {
      const [url] = urls.splice(index, 1)
      URL.revokeObjectURL(url)
    }
  }

  onUnmounted(() => {
    urls.forEach(URL.revokeObjectURL)
  })
  return {
    createUrl,
    removeUrl,
  }
}
