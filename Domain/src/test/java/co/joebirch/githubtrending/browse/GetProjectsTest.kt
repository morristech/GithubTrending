package co.joebirch.githubtrending.browse

import co.joebirch.domain.executor.PostExecutionThread
import co.joebirch.domain.interactor.browse.GetProjects
import co.joebirch.domain.model.Project
import co.joebirch.domain.repository.ProjectsRepository
import co.joebirch.githubtrending.test.ProjectDataFactory
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetProjectsTest {

    private lateinit var getProjects: GetProjects
    @Mock lateinit var projectsRepository: ProjectsRepository
    @Mock lateinit var postExecutionThread: PostExecutionThread

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getProjects = GetProjects(projectsRepository, postExecutionThread)
    }

    @Test
    fun getProjectsCompletes() {
        stubProjectsRepositoryGetProjects(
                Single.just(ProjectDataFactory.makeProjectList(2)))

        val testObserver = getProjects.buildUseCaseSingle().test()
        testObserver.assertComplete()
    }

    @Test
    fun getProjectsCallsRepository() {
        stubProjectsRepositoryGetProjects(
                Single.just(ProjectDataFactory.makeProjectList(2)))

        getProjects.buildUseCaseSingle().test()
        verify(projectsRepository).getProjects()
    }

    @Test
    fun getProjectsReturnsData() {
        val projects = ProjectDataFactory.makeProjectList(2)
        stubProjectsRepositoryGetProjects(
                Single.just(projects))

        val testObserver = getProjects.buildUseCaseSingle().test()
        testObserver.assertValue(projects)
    }

    private fun stubProjectsRepositoryGetProjects(single: Single<List<Project>>) {
        whenever(projectsRepository.getProjects())
                .thenReturn(single)
    }

}