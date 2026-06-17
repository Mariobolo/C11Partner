// Bootstrap风格壁纸滑动管理器
class WallpaperSwipeManager {
    constructor() {
        this.backgroundElement = null;
        this.containerElement = null;
        this.isAnimating = false;
        this.startX = 0;
        this.currentX = 0;
        this.velocity = 0;
        this.lastTime = 0;
        
        // Bootstrap淡入淡出动画参数
        this.fadeDuration = 800;
        this.swipeThreshold = 30;
        this.velocityThreshold = 0.3;
        
        this.init();
    }
    
    init() {
        this.backgroundElement = document.querySelector('.background-image');
        this.containerElement = document.querySelector('.background-container');
        
        if (!this.backgroundElement) {
            console.warn('壁纸元素(.background-image)未找到');
            return;
        }
        
        if (!this.containerElement) {
            console.warn('壁纸容器(.background-container)未找到');
            return;
        }
        
        this.bindEvents();
        console.log('Bootstrap壁纸滑动管理器已初始化');
    }
    
    bindEvents() {
        this.containerElement.addEventListener('touchstart', this.handleTouchStart.bind(this), { passive: false });
        this.containerElement.addEventListener('touchmove', this.handleTouchMove.bind(this), { passive: false });
        this.containerElement.addEventListener('touchend', this.handleTouchEnd.bind(this), { passive: false });
        
        this.containerElement.addEventListener('mousedown', this.handleMouseDown.bind(this));
        document.addEventListener('mousemove', this.handleMouseMove.bind(this));
        document.addEventListener('mouseup', this.handleMouseUp.bind(this));
    }
    
    handleTouchStart(e) {
        if (e.touches.length !== 1 || this.isAnimating) return;
        
        this.startX = e.touches[0].clientX;
        this.currentX = this.startX;
        this.velocity = 0;
        this.lastTime = Date.now();
        
        e.preventDefault();
    }
    
    handleTouchMove(e) {
        if (e.touches.length !== 1 || this.isAnimating) return;
        
        const currentTime = Date.now();
        const clientX = e.touches[0].clientX;
        const deltaX = clientX - this.currentX;
        const deltaTime = currentTime - this.lastTime;
        
        if (deltaTime > 0) {
            this.velocity = deltaX / deltaTime;
        }
        
        this.currentX = clientX;
        this.lastTime = currentTime;
        
        this.applyDragTransform(deltaX);
        e.preventDefault();
    }
    
    handleTouchEnd(e) {
        if (this.isAnimating) return;
        
        const deltaX = this.currentX - this.startX;
        const distance = Math.abs(deltaX);
        
        if (distance > this.swipeThreshold || Math.abs(this.velocity) > this.velocityThreshold) {
            if (deltaX > 0) {
                this.animateToNextWallpaper('right', this.velocity);
            } else {
                this.animateToNextWallpaper('left', this.velocity);
            }
        } else {
            this.resetOpacity();
        }
    }
    
    handleMouseDown(e) {
        this.isMouseDown = true;
        this.startX = e.clientX;
        this.currentX = this.startX;
        this.velocity = 0;
        this.lastTime = Date.now();
        e.preventDefault();
    }
    
    handleMouseMove(e) {
        if (!this.isMouseDown || this.isAnimating) return;
        
        const currentTime = Date.now();
        const deltaX = e.clientX - this.currentX;
        const deltaTime = currentTime - this.lastTime;
        
        if (deltaTime > 0) {
            this.velocity = deltaX / deltaTime;
        }
        
        this.currentX = e.clientX;
        this.lastTime = currentTime;
        
        this.applyDragTransform(deltaX);
        e.preventDefault();
    }
    
    handleMouseUp(e) {
        if (!this.isMouseDown) return;
        this.isMouseDown = false;
        this.handleTouchEnd({ touches: [{ clientX: this.currentX }] });
    }
    
    applyDragTransform(deltaX) {
        const absDeltaX = Math.abs(deltaX);
        const maxDragDistance = 100;
        const opacity = Math.max(0.7, 1 - absDeltaX / maxDragDistance);
        this.backgroundElement.style.opacity = opacity.toString();
    }
    
    resetOpacity() {
        this.backgroundElement.style.transition = 'opacity 0.2s ease-in-out';
        this.backgroundElement.style.opacity = '1';
        
        setTimeout(() => {
            this.backgroundElement.style.transition = '';
        }, 200);
    }
    
    animateToNextWallpaper(direction, initialVelocity = 0) {
        if (this.isAnimating) return;
        this.isAnimating = true;
        
        this.getNextWallpaper(direction, (nextWallpaperUrl) => {
            if (nextWallpaperUrl) {
                this.executeFadeTransition(nextWallpaperUrl, direction);
            } else {
                this.resetAnimationState();
            }
        });
    }
    
    executeFadeTransition(nextWallpaperUrl, direction) {
        this.backgroundElement.style.transition = `opacity ${this.fadeDuration/2}ms ease-in-out`;
        this.backgroundElement.style.opacity = '0';
        
        setTimeout(() => {
            this.backgroundElement.style.backgroundImage = `url('${nextWallpaperUrl}')`;
            this.backgroundElement.style.opacity = '1';
            
            setTimeout(() => {
                this.resetAnimationState();
                showToast(direction === 'right' ? '上一张壁纸' : '下一张壁纸');
            }, this.fadeDuration/2);
        }, this.fadeDuration/2);
    }
    
    resetAnimationState() {
        this.isAnimating = false;
        this.backgroundElement.style.transition = '';
        this.backgroundElement.style.opacity = '1';
    }
    
    getNextWallpaper(direction, callback) {
        if (typeof Android !== 'undefined' && Android.getRandomWallpaperBase64Async) {
            const callbackId = AsyncCallbackManager.register((wallpaperBase64) => {
                if (wallpaperBase64 && wallpaperBase64 !== "") {
                    const imageUrl = `data:image/jpeg;base64,${wallpaperBase64}`;
                    if (callback) callback(imageUrl);
                } else {
                    if (callback) callback(null);
                }
            });
            Android.getRandomWallpaperBase64Async(callbackId);
        } else {
            setTimeout(() => {
                const mockImages = [
                    'images/default_bg_1.jpg',
                    'images/nav_air.png',
                    'images/nav_apps.png',
                    'images/nav_byds.png'
                ];
                const randomImage = mockImages[Math.floor(Math.random() * mockImages.length)];
                if (callback) callback(randomImage);
            }, 100);
        }
    }
    
    nextWallpaper() {
        if (typeof Android !== 'undefined' && Android.getRandomWallpaperBase64Async) {
            const callbackId = AsyncCallbackManager.register((wallpaperBase64) => {
                if (wallpaperBase64 && wallpaperBase64 !== "") {
                    this.setBackgroundImage(`url('data:image/jpeg;base64,${wallpaperBase64}')`);
                    showToast('下一张壁纸');
                }
            });
            Android.getRandomWallpaperBase64Async(callbackId);
        }
    }
    
    previousWallpaper() {
        if (typeof Android !== 'undefined' && Android.getRandomWallpaperBase64Async) {
            const callbackId = AsyncCallbackManager.register((wallpaperBase64) => {
                if (wallpaperBase64 && wallpaperBase64 !== "") {
                    this.setBackgroundImage(`url('data:image/jpeg;base64,${wallpaperBase64}')`);
                    showToast('上一张壁纸');
                }
            });
            Android.getRandomWallpaperBase64Async(callbackId);
        }
    }
    
    setBackgroundImage(imageUrl) {
        this.backgroundElement.style.transition = 'opacity 0.3s ease-in-out';
        this.backgroundElement.style.opacity = '0';
        
        setTimeout(() => {
            this.backgroundElement.style.backgroundImage = imageUrl;
            this.backgroundElement.style.opacity = '1';
        }, 150);
    }
}

let wallpaperSwipeManager = null;

document.addEventListener('DOMContentLoaded', () => {
    wallpaperSwipeManager = new WallpaperSwipeManager();
});

window.WallpaperSwipeManager = WallpaperSwipeManager;