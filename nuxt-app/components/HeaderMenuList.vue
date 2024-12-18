<script setup lang="ts">
const props = defineProps<{ isActive: boolean }>()
const emits = defineEmits<{ (e: 'closeMenu'): void }>()

const navList: readonly NavItem[] = useCategoryStore().getNavList()

const route = useRoute()
const isInternalActive = computed(() => props.isActive || route.path === '/')
const isActiveOverLay = computed(() => isInternalActive && props.isActive)
</script>

<template>
	<transition>
		<div v-if="isInternalActive" class="header-nav-wrapper">
			<div
				class="overlay"
				v-if="isActiveOverLay"
				@click="emits('closeMenu')"
			></div>
			<div class="container">
				<div class="row">
					<div class="col-25">
						<nav class="header-nav">
							<ul class="nav-list" @click="emits('closeMenu')">
								<li
									class="nav-item"
									v-for="root in navList"
									:key="root.id"
								>
									<NuxtLink class="nav-link" :to="root.href">
										<i :class="root.icon"></i>
										{{ root.name }}</NuxtLink
									>
									<ul
										class="sub-nav-list"
										v-if="root.children"
									>
										<li
											class="sub-nav-item"
											v-for="child in root.children"
											:key="child.id"
										>
											<NuxtLink
												class="sub-nav-link"
												:to="child.href"
											>
												{{ child.name }}
											</NuxtLink>
											<ul
												class="sub-nav-list"
												v-if="child.children"
											>
												<li
													class="sub-nav-item"
													v-for="subChild in child.children"
												>
													<NuxtLink
														:to="subChild.href"
													>
														{{ subChild.name }}
													</NuxtLink>
												</li>
											</ul>
										</li>
									</ul>
								</li>
							</ul>
						</nav>
					</div>
				</div>
			</div>
		</div>
	</transition>
</template>

<style scoped lang="scss">
@use '@/assets/scss/components/header-nav';

.v-enter-active,
.v-leave-active {
	transition: opacity 0.1s ease;
}

.v-enter-from,
.v-leave-to {
	opacity: 0;
}
</style>
